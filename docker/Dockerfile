# Use a modern JDK 11 base image
FROM openjdk:8u151-jdk-alpine3.7

# Expose Spring Boot default port (8070)
EXPOSE 8070

# App directory
ENV APP_HOME /usr/src/app
WORKDIR $APP_HOME

# Copy built jar
COPY target/shopping-cart-0.0.1-SNAPSHOT.jar app.jar

# Set JVM options (heap size)
ENV JAVA_OPTS="-Xms128m -Xmx512m"

# Correct ENTRYPOINT: pass JAVA_OPTS
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
