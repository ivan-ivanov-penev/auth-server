FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD

WORKDIR /tmp/auth-server

COPY ./src ./src
COPY ./pom.xml ./

# Set externally by build-server (jenkins)
ARG app_version=latest

RUN mvn clean package -Dapp.version=$app_version -Dmaven.test.skip=true

FROM openjdk:11-jre-slim

COPY --from=MAVEN_BUILD /tmp/auth-server/target/auth-server-*.jar /opt/reinno/auth-server/
COPY --from=MAVEN_BUILD /tmp/auth-server/src/main/resources/application.yaml /etc/reinno/auth-server/
COPY --from=MAVEN_BUILD /tmp/auth-server/src/test/resources/private.p12 /etc/reinno/auth-server/

RUN groupadd -r reinno
RUN useradd -r -M -g reinno auth-server
RUN mkdir /var/log/reinno
RUN chown -R auth-server:reinno /opt/reinno
RUN chown -R auth-server:reinno /etc/reinno
RUN chown -R auth-server:reinno /var/log/reinno

USER auth-server:reinno

ENV SPRING_CONFIG_LOCATION="/etc/reinno/auth-server/"
ENV TZ="GMT"

# optionally the JVM TimeZone can be set adding below: -Duser.timezone="GMT"
CMD java -jar /opt/reinno/auth-server/auth-server-*.jar
