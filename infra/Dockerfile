FROM openjdk:17-slim as build-stage
WORKDIR /workspace/app
COPY target/*.jar target/app.jar
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build-stage ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build-stage ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build-stage ${DEPENDENCY}/BOOT-INF/classes /app
RUN addgroup -system spring-group && adduser -system spring-user
USER spring-user:spring-group
ENTRYPOINT ["java","-cp","app:app/lib/*","com.fleetmanagement.FleetManagementApplication"]