FROM mcr.microsoft.com/java/maven:8u192-zulu-debian9 AS build-env
WORKDIR /app
COPY . /app
RUN mvn package -DskipTests=true
RUN ls -lah /app/target
RUN jar -xf /app/target/*.jar
RUN ls -lah /app/

FROM mcr.microsoft.com/java/jdk:8-zulu-alpine
# ENV SPRING_APPLICATION_JSON='{"spring":{"datasource":{"url":"jdbc:oracle:thin:@//10.0.2.15:32118/XEPDB1"}}}'
COPY --from=build-env /app/BOOT-INF/lib /app/lib
COPY --from=build-env /app/META-INF /app/META-INF
COPY --from=build-env /app/BOOT-INF/classes /app
VOLUME /opt/payment/file
EXPOSE 8098/tcp
ENTRYPOINT ["java","-cp","app:app/lib/*","com.bank.payment.PagamentoApplication"]