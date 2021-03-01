# App Microservice

I have used wiremock to mock orderservice and catalogservice

## Requirements

Java - 1.8


**1. Build **

```bash
mvn clean install
java -jar target/app-1.0.0.jar

```

* The app will start running at <http://localhost:8000> and provide below API's


1.http://localhost:8000/customer/{custId}/order/summary -  Gives Order Summary based on customer Id

	i.e http://localhost:8000/customer/cust01/order/summary

2.http://localhost:8000/order/{orderId}/summary - Order Summary based on order Id

	i.e http://localhost:8000/order/1/summary

This above API Provide .

3.http://localhost:8000/order/{orderId}/detail - Order Details based on order Id

	i.e http://localhost:8000/order/1/detail
