FROM openjdk:17-jdk-alpine
ADD target/currencyExchange-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar currencyExchange-0.0.1-SNAPSHOT.jar