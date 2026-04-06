FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY database ./database

RUN mvn clean package

CMD ["java", "-cp", "target/classes", "org.example.ShoppingCartApp"]