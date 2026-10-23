WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn/ .mvn/

RUN chmod +x mvnw

COPY src/ src/

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 3006

ENTRYPOINT ["java", "-jar", "app.jar"]