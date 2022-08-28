ARG OPENJDK_TAG=11
FROM openjdk:${OPENJDK_TAG}

ARG SBT_VERSION=1.7.1

WORKDIR /app

# Install sbt
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" |  tee /etc/apt/sources.list.d/sbt.list \
    && echo "deb https://repo.scala-sbt.org/scalasbt/debian /" |  tee /etc/apt/sources.list.d/sbt_old.list \
    && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add \
    && apt-get update \
    && apt-get install sbt

WORKDIR /src/main/java
COPY . /src/main/java
RUN sbt clean compile test
EXPOSE 9000
ENTRYPOINT ["sbt", "run"]