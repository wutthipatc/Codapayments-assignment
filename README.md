# Codapayments-assignment
A Spring boot Java project creating round-robin call to actual service using service discovery pattern

## Preparation
* Clone the repository which contains two Spring boot Maven projects.
* Install Docker desktop runtime and docker-compose on your local machine.
* Install Maven on your local machine.
* Install Postman on your local machine.
* Import Postman collection file name **Coda payments.postman_collection.json** on repository root directory to the Postman.
## Running the application
* Go to project directory **round-robin** and **application**, then run command mvn clean package on both.
* Go to repository root directory which contains **docker-compose.yml** and **docker-compose-create-replica.yml**.
* Run command `docker-compose` up to create **Round-robin server** and **3 Application servers**.
* Open the Postman collection imported above and call **External api request**. This will have the **Round-robin server** call the **Application server** which run on port **8081, 8082 and 8083** respectively in **Round-robin fashion**.
## How to test
* You can try calling the Postman **request Change application config** with field name **processingDuration(deafult value 200 ms)**, **slowDuration(default value 1000 ms)** and **resetIsSlowDuration(deafult value 200 ms)** to mock up and change some configuration on **Application server** with specific port number.
* The explanation for each field is **processingDuration** => the time to mock the processing time in ms on **Application server** when being called from **Round-robin server** with **External api request**, **slowDuration** => the time to identify whether the specific **Application server** for the one specific endpont called by **Round-robin server** is slow or not (If the **processingDuration value** is greater than this value, this particular **Application server** with the provided endpoint will be treated as slow), **resetIsSlowDuration** => the reset duration in ms to set back the slow flag to false, this will lead to **Round-robin server** being able to call this **Application server** again.
* Try playing around with **Change application config request** (The recommended values are provided by the Postman collection to see how The **Round-robin server** works if some **Application servers** are called with slow processing) and calling **External api request** on Postman collection provided.
* You can also try adding a new replica of **Application server** on your specific port using this command `REPLICA_NUM=4 REPLICA_PORT=8084 docker-compose -f docker-compose-create-replica.yml up` which will create **the fourth instance** of **Application server on the port 8084** and let's call the **External api** again.
## Note
* If you want to turn off the log which is the heartbeat request receiving from each **Application server** on **Round-robin server**, you can go to **round-robin project** and remove the l**og line 64 on ServiceRegisterService**, then run `mvn clean package` again and start the docker-compose with command `docker-compose up --build` to re-trigger the Dockerfile copying the new .jar file.
* If you found any problem or concern you can contact me directly with email moderndogs@gmail.com or via TA, thanks.
