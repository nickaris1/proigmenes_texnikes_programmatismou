FROM adoptopenjdk/openjdk13-openj9:latest
MAINTAINER ece.upatras.gr
COPY ./jar-with-dependencies.jar /opt/rest.example/
WORKDIR /opt/rest.example/
CMD ["java", "-jar", "/opt/rest.example/jar-with-dependencies.jar"]
EXPOSE 8888