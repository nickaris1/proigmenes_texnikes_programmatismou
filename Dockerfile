FROM adoptopenjdk/openjdk13-openj9:latest
MAINTAINER ece.upatras.gr
COPY ./jar-with-dependencies.jar /opt/akinita/
COPY ./database/database.db /opt/akinita/database/database.db
WORKDIR /opt/akinita/
CMD ["java", "-jar", "/opt/akinita/jar-with-dependencies.jar"]
EXPOSE 8888