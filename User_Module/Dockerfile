FROM openjdk:11
ADD target/user-login-module.jar user-login-module.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "user-login-module.jar"]