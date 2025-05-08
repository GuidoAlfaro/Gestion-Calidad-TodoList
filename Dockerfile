# Usa JDK 17 como base
FROM eclipse-temurin:17-jdk-alpine

# Crea un directorio para la app
WORKDIR /app

# Copia el JAR generado (ajusta el nombre si es diferente)
COPY todolist-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto donde corre Spring Boot
EXPOSE 8081

# Comando para ejecutar el backend
ENTRYPOINT ["java", "-jar", "app.jar"]
