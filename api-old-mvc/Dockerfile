FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM tomcat:10.1-jdk21
COPY --from=builder /app/target/student-sync-api.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080