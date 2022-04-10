# forex-proxy

Preinstallation requirements:
- Docker

## How to run tests and generate report

Windows users
> gradlew.bat test --info

Linux/Mac users
> ./gradlew test --info

Test report can be found in this path **app/build/reports/tests/test/index.html**

## How to run locally without docker

Windows users
> gradlew.bat run

Linux/Mac users
> ./gradlew run

Required these environment variables
- FX_PROXY_BASE_URL
- FX_PROXY_HTTP_TOKEN
- FX_PROXY_HTTP_TIMEOUT
- FX_PROXY_APP_PORT
- FX_PROXY_APP_SECRET

Environment variables sample can be found in this path **app/src/main/resources/.env**
 
 This forex-proxy also depends on https://hub.docker.com/r/paidyinc/one-frame service.

 ## How to run with docker compose

Run forex-proxy with all its dependencies
 > docker-compose up

To shutdown
 > docker-compose down

 If there is **unexpected EOF error** while executing docker-compose up, just retry.

 The configuration for docker compose can be found in this file **docker-compose.yml**

 docker-compose up will pull external images and build forex-proxy image and run all the services.

 docker-compose up will run **10 one-frame instances** to cover 10000 requests per day

 nginx is used for load balancer to distribute the request to all one-frame instances using **round-robin**

 ## How to use forex-proxy

 ### API

> GET /rates?pair={currency_pair_0}&pair={currency_pair_1}&...pair={currency_pair_n}

Example:

Request
> curl -H "token: d623ec59-118f-4e8c-a874-653b127af546" "localhost:7070/rates?pair=GBPUSD&pair=EURUSD&pair=USDJPY"

Response 200
> {"fxRates":[{"from":"USD","to":"JPY","bid":0.6118225421857174,"ask":0.8243869101616611,"price":0.7181047261736893,"time_stamp":"2022-04-10T08:04:40.94Z"},{"from":"EUR","to":"USD","bid":0.8435259660697864,"ask":0.4175532166907524,"price":0.6305395913802694,"time_stamp":"2022-04-10T08:04:40.94Z"},{"from":"GBP","to":"USD","bid":0.1350922166954046,"ask":0.13871074418376472,"price":0.13690148043958467,"time_stamp":"2022-04-10T08:04:40.94Z"}]}

Request
> curl -H "token: invalidToken" "localhost:7070/rates?pair=GBPUSD&pair=EURUSD&pair=USDJPY"

Response 400
> {"errorMessage":"Invalid token!"}

**Token is required for authentication**. Token is configurable for forex-proxy

## Supported currencies are:
- AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD