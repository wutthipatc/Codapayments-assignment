package com.codapayments.roundrobin.controller.register;

import com.codapayments.roundrobin.dto.register.request.ServiceRegisterRequest;
import com.codapayments.roundrobin.dto.register.response.RegisterAckResponse;
import com.codapayments.roundrobin.service.ServiceRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("register")
public class ServiceRegisterController {
    private final ServiceRegisterService service;
    @PostMapping
    public RegisterAckResponse register(@RequestBody ServiceRegisterRequest request) {
        return service.register(request);
    }

}
