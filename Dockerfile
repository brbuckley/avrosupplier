FROM openjdk:11

COPY target/avrosupplier.jar /target/avrosupplier.jar

EXPOSE 8080

CMD ["java","-jar","-Dspring.profiles.active=deploy","/target/avrosupplier.jar"]
