#!/bin/sh
mvn dependency:purge-local-repository
mvn clean
mvn package
cd target && java -jar tormessenger-0.0.1-SNAPSHOT-jar-with-dependencies.jar
