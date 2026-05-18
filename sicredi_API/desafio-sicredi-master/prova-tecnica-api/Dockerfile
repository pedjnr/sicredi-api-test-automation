FROM maven:3.8.6-openjdk-11

WORKDIR /home/app

COPY pom.xml .
COPY src src

CMD ["mvn", "spring-boot:run"]
