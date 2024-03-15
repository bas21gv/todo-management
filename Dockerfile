FROM openjdk:17
WORKDIR /app
COPY target/todo-management.jar .
EXPOSE 8080
CMD ["java", "-jar", "todo-management.jar"]