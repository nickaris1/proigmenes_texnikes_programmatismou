FROM adoptopenjdk/openjdk13-openj9:latest
MAINTAINER ece.upatras.gr
COPY ./rest.example-0.0.1-SNAPSHOT-exec.jar /opt/rest.example/
WORKDIR /opt/rest.example/
CMD ["java", "-jar", "/opt/rest.example/jar-with-dependencies.jar"]
EXPOSE 8888