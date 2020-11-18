# OS metrics collector

This project is example of using Kafka producer/consumer.
It takes some operation system metrics and puts them into PostgreSQL database through Kafka broker

### Project structure
- 'common' - contains common code and model for the consumer and producer
- 'consumer' - consumer implementation
- 'producer' - producer implementation

### Requirements and how to build
Before building the project make sure that you have installed:
 - 'JDK' version >= 11
 - PostgreSQL database
 - Kafka server version >= 2.3

To build the project run './gradlew clean installDist' command

*To prepare you SSL certificates you need to obtain following files:*
- 'Access Key' - expected file name is: 'service.key'
- 'Access Certificate' - expected file name is: 'service.cert'
- 'CA Certificate' - expected file name is: 'ca.pem'- there should be two - one for the Kafka, one for the PostgreSql

Keystore and truststore for holding the keys and certificates can be created with the following commands:

openssl pkcs12 -export -inkey service.key -in service.cert -out client.keystore.p12 -name service_key
keytool -import -file ca.pem -alias CA -keystore client.truststore.jks

### To run Kafka consumer
To run Kafka consumer you need:

1. Execute following on you PostgreSQL server:
	
	create database metricsdb;
	create user metricsapp with password 'metricsapp';
	grant all privileges on database metricsdb to metricsapp;

2. Create a kafka topic, default kafka topic is: 'os-metrics'.
if you want to use another name please see the full list of parameters in the 'consumer/consumer.conf file

3. Change parameters  'consumer/consumer.conf: 
    bootstrap.servers - comma-separated list of Kafka brokers
    db.jdbc.url - PgSQL jdbc URL to the database
    db.user - application user name
    db.password - application user password
    omc.topicname - Kafka topic name
    
### To run Kafka producer
To run kafka producer you need:

1. Create a kafka topic, default kafka topic is: 'os-metrics'. 
if you want to use another name please see the full list of parameters in the 'producer/producer.conf' file

2. Change environment variables in the 'producer/producer.conf' file
    bootstrap.servers - comma-separated list of Kafka brokers
    omc.hostname - producer host name
    omc.ipAddress - producer IP address
    omc.topicname - Kafka topic name
 
Configs should be visible for apps - e.g. put them into corresponding folders.

