# Java curl receiving application running on AWS EC2 instance.

# to use the script
chmod +x compileAndRun.sh
run script in background "daemonized"
`nohup bash compileAndRun.sh &`

## Required packages
you will need to have the following installed
java openjdk 11+
maven 3.6+
git
mysql install, i used https://dev.mysql.com/doc/mysql-getting-started/en/

## Intro
You can perform curl actions against the application by doing the below commands for testing.
This application is meant to satisfy base requirements for ECE 531 Homework assignment "Communication in the Cloud." I can attest that i do not check hard for malformed requests or duplication of data to the database. 

All required applications dependencies are listed in the pom file.

## Database
Simple database created for this application. Database name is "consoles." Contains a single table with id and name columns. It is only mean to prove the functionality of the java code.

## AWS EC2
Currently only allow port 8080 traffic, port ssh to my IP. 
Public IP for this instance with httpdnano running is
http://52.8.135.131:8080

## Commands
get index
`curl http://52.8.135.131:8080/ # provides a list of all objects`

get by id
`curl http://52.8.135.131:8080/id # to query object based on id`

post
`curl -X POST http://52.8.135.131:8080/ -d NAME`

put
`curl -X PUT http://52.8.135.131:8080/ -d NAME`

delete
`curl -X DELETE http://52.8.135.131:8080/id`


## Running the code
`mvn compile`
`mvn exec:java -Dexec.mainClass="com.hilland.HillandCurlServer"`
