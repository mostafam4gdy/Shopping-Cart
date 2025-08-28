FROM openjdk:8u151-jdk-alpine3.7

EXPOSE 8070

ENV APP_HOME /usr/src/app

COPY target/shopping-cart-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

WORKDIR $APP_HOME

# Set JVM opts with sensible limits
ENV JAVA_OPTS="-Xms128m -Xmx512m"

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
