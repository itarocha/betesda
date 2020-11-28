FROM adoptopenjdk/openjdk8:latest
ADD target/betesda.jar betesda.jar
EXPOSE 8088
ENTRYPOINT [ "java", "-jar" "/betesda.jar" ]