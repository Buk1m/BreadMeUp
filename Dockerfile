FROM openjdk:11-jdk
ADD target/BreadMeUp-1.0.0-RELEASE.jar BreadMeUp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "BreadMeUp.jar"]