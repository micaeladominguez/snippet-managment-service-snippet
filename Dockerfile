ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

FROM gradle:7.6.1-jdk17
COPY . /home/gradle/src
WORKDIR /home/gradle/src

ENV GITHUB_ACTOR=$GITHUB_ACTOR
ENV GITHUB_TOKEN=$GITHUB_TOKEN
RUN gradle build
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/snippet-managment-service-0.0.1-SNAPSHOT.jar"]
