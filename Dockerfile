FROM openjdk:11-jdk
ADD target/BreadMeUp-0.0.1-SNAPSHOT.jar BreadMeUp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "BreadMeUp.jar"]