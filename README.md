# REST Adapter

## Description

This project works like a mocked version of Supplier C (JMS+Avro). The idea is to keep it as simple as possible and provide the needed responses for Adapter C.

## How to Run

You can run with your favorite IDE using SupplierApplication.main(), but since it's a RabbitMQ dependant project, it's recommended to run it along with a broker. You can also use maven: `mvn spring-boot:run`, or run it directly with the jar file:


```
mvn clean install
java -jar target/avrosupplier.jar
```

## Configuration

As this is a simple project, no extra configuration should be needed. The default profile should be enough to run the project as standalone.
