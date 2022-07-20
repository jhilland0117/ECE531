# Java curl receiving application running on AWS EC2 instance.

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

## Commands
get
`curl http://52.8.135.131:8080/ # provides a list of all objects`
`curl http://52.8.135.131:8080/id # to query object based on id`

post
`curl -X POST http://52.8.135.131:8080/ -d "PS5"`

put


delete
`curl -X DELETE http://52.8.135.131:8080/id`

