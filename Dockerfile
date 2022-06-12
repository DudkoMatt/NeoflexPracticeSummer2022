FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY churl.sh churl.sh
CMD /bin/sh -c "\
chmod +x ./churl.sh && \
java -jar /app.jar \
--spring.profiles.active=prod \
--spring.datasource.url=$(echo $(./churl.sh ${DATABASE_URL} | cut -d' ' -f1)) \
--spring.datasource.username=$(echo $(./churl.sh ${DATABASE_URL} | cut -d' ' -f2)) \
--spring.datasource.password=$(echo $(./churl.sh ${DATABASE_URL} | cut -d' ' -f3)) \
--server.port=${PORT}"
