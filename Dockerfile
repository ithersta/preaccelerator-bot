FROM eclipse-temurin:18-jre

RUN apt-get update \
        && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends postgresql-client-14

USER 1000

ENTRYPOINT ["/preaccelerator-bot-1.0-SNAPSHOT/bin/preaccelerator-bot"]

ADD ./build/distributions/preaccelerator-bot-1.0-SNAPSHOT.tar /
