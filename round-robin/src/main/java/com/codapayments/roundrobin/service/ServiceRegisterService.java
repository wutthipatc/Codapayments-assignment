package com.codapayments.roundrobin.service;

import com.codapayments.roundrobin.dto.register.request.ServiceRegisterRequest;
import com.codapayments.roundrobin.dto.register.response.RegisterAckResponse;
import com.codapayments.roundrobin.model.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class ServiceRegisterService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegisterService.class);
    private static final Integer HEART_BEAT_INTERVAL_MILLI = 2000;
    private static final Integer HEART_BEAT_ACCEPT_ERROR_MILLI = 2500;
    private final Map<Integer, ServiceInfo> serviceStatusMap;

    public ServiceRegisterService() {
        this.serviceStatusMap = new HashMap<>();
    }
    private Optional<Tuple2<Integer, ServiceInfo>> getAvailableServiceWithNextIndexKeyRecurse(Integer index, Integer count, Integer serviceStatusMapSize) {
        final Predicate<ServiceInfo> isServiceAvailable = serviceInfo -> !serviceInfo.getIsSlow() &&
                serviceInfo.getLastUpdateTime().plusMillis(HEART_BEAT_INTERVAL_MILLI + HEART_BEAT_ACCEPT_ERROR_MILLI).isAfter(Instant.now());
        if (count > serviceStatusMapSize) return Optional.empty();
        else {
            Optional<Tuple2<Integer, ServiceInfo>> resultTuple = Optional.ofNullable(serviceStatusMap.get(index))
                    .filter(isServiceAvailable)
                    .map(serviceInfo -> {
                        if (Objects.equals(index, serviceStatusMapSize)) return Tuples.of(1, serviceInfo);
                        else return Tuples.of(index + 1, serviceInfo);
                    });
            if (resultTuple.isPresent())
                return resultTuple;
            else
                return getAvailableServiceWithNextIndexKeyRecurse(
                        Objects.equals(index, serviceStatusMapSize) ? 1 : index + 1,
                        count + 1,
                        serviceStatusMapSize
                );
        }
    }
    public Optional<Tuple2<Integer, ServiceInfo>> getAvailableServiceInfoWithNextIndexByIndexKey(Integer index) {
        final int serviceStatusMapSize = serviceStatusMap.size();
        if (serviceStatusMapSize != 0)
            return getAvailableServiceWithNextIndexKeyRecurse(index, 1, serviceStatusMapSize);
        else return Optional.empty();
    }
    public RegisterAckResponse register(ServiceRegisterRequest request) {
        final ServiceInfo serviceInfo = ServiceInfo.fromServiceRegisterReq(request);
        updateServiceStatusMap(serviceInfo);
        return RegisterAckResponse.getSuccessResponse(serviceInfo.getName(), serviceInfo.getLastUpdateTime());
    }
    // Must be synchronized to avoid race condition
    private synchronized void updateServiceStatusMap(ServiceInfo info) {
//        serviceStatusMap.forEach((key, value) -> System.out.printf("State map with key: %d, value: %s%n", key, value));
        logger.info("ServiceRegisterService::updateServiceStatusMap with service info: {}", info);
        Optional<Map.Entry<Integer, ServiceInfo>> foundEntryOption = serviceStatusMap.entrySet().stream()
                .filter(entry -> entry.getValue().getName().equals(info.getName()))
                .findFirst();
        if (foundEntryOption.isPresent()) {
            // Update instant on found element
            Map.Entry<Integer, ServiceInfo> entry = foundEntryOption.get();
            Integer key = entry.getKey();
            ServiceInfo foundServiceInfo = entry.getValue();
            ServiceInfo updateServiceInfo = foundServiceInfo.copy(
                    Optional.empty(),
                    Optional.of(info.getBaseUrl()),
                    Optional.of(info.getLastUpdateTime()),
                    Optional.of(info.getIsSlow())
            );
            serviceStatusMap.put(key, updateServiceInfo);
        }
        else
            // Add service info for first time registering index based 1
            serviceStatusMap.put(serviceStatusMap.size() + 1, info);

    }
}
