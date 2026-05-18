FROM maven:3.8.6-openjdk-11

WORKDIR /home/app

COPY pom.xml .
COPY src src

CMD ["sh", "-c", "sleep 60 && mvn test -Denv=docker; status=$?; mvn allure:report || true; exit $status"]
