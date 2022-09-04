FROM maven:3-jdk-11 AS builder
WORKDIR /app
COPY pom.xml /app
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src /app/src
RUN mvn -B clean package

FROM jetty:9.4-jre11-openjdk
ENV JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8
COPY --from=builder /app/target/ProjectByElvara-1.0-SNAPSHOT.war /var/lib/jetty/webapps/root.war
CMD java -jar "$JETTY_HOME/start.jar"