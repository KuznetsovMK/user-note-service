FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /user-note/server
COPY . /user-note/server/.
RUN mvn -f /user-note/server/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /user-note/server
COPY --from=builder /user-note/server/target/*.jar /user-note/server/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/user-note/server/*.jar"]