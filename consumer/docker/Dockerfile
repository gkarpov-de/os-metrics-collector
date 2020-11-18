FROM openjdk:11-alpine

RUN apt-get update && apt-get -y install openssl

ENV BOOTSTRAP_SERVERS=
ENV DB_JDBC_URL=
ENV DB_PASSWORD=
ENV DB_SSL=
ENV DB_SSLMODE=
ENV DB_SSLROOTCERT=
ENV DB_USER=
ENV FLYWAY_MIGRATION_LOCATIONS="/home/app/consumer/conf"
ENV FLYWAY_MIGRATION_PASSWORD=
ENV FLYWAY_MIGRATION_USER=
ENV GROUP_ID=
ENV OMC_POLLDURATION="1000ms"
ENV OMC_THREADS_QTY=1
ENV OMC_TOPICNAME=
ENV SECURITY_PROTOCOL=
ENV SSL_ENDPOINT_IDENTIFICATION_ALGORITHM=
ENV SSL_KEY_PASSWORD=
ENV SSL_KEYSTORE_LOCATION=
ENV SSL_KEYSTORE_PASSWORD=
ENV SSL_KEYSTORE_TYPE=
ENV SSL_TRUSTSTORE_LOCATION=
ENV SSL_TRUSTSTORE_PASSWORD=

#JDBC URL
ENV CONSUMER_JDBC_URL=""
#Database applcation user name, which consumer app uses to store topica data in the database
ENV CONSUMER_DB_APP_USER=""
#Database applcation user password
ENV CONSUMER_DB_APP_PWD=""
#Path where Flyway SQL migration scripts are located
ENV CONSUMER_DB_FLYWAY_LOCATIONS="/home/app/consumer/conf"
#Database migration user, which consumer app uses to migrate your database
ENV CONSUMER_DB_FLYWAY_USER=""
#Database migration user pwaaword
ENV CONSUMER_DB_FLYWAY_PWD=""
#kafka consumer poll timout in milliseconds
ENV CONSUMER_POLL="1000ms"
#Amount of threads per each consumer
ENV CONSUMER_THREADS_AMOUNT=1
#Kafka topic name
ENV CONSUMER_TOPIC_NAME="os-metrics"
#Kafka brokers, comma-separated list
ENV KAFKA_BROKERS=""

WORKDIR /home/app

COPY ./consumer/build/install /home/app
COPY ./consumer/docker/run-consumer /home/app

RUN mkdir -p /home/app/consumer/conf
COPY ./conf/ /home/app/consumer/conf

CMD sh consumer


