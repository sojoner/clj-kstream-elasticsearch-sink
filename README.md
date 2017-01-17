# clj-kstream-elasticsearch-sink

A Clojure tool designed to read from a kafka topic and index the
items into elasticsearch

## Docker Hub

* [sojoner/clj-kstream-elasticsearch-sink](https://hub.docker.com/r/sojoner/clj-kstream-elasticsearch-sink/) 

## Requirements

* [leiningen 2.7.1](https://leiningen.org/)
* [elasticsearch 1.X](https://www.elastic.co/)
* [kafka 0.10.0.1](http://kafka.apache.org) 
* [docker 1.12.6](https://www.docker.com/)

### Kafka Topic

The topic you are consuming needs to have **^String** Keys and **^String** .json values.  

## Build Clojure
    
    $lein check

## Build .jar

    $lein uberjar

## Build .container
    
    $cd deploy
    $./containerize.sh

## Usage Leiningen

    $lein run --broker kafka-broker:9092 --topic mapped-test-json --elasticsearch http://elasticsearch:9200 --index 2017-hpc-test --index-type hpc-test

## Usage java

    $java - jar clj-kstream-elasticsearch-sink-0.1.0-SNAPSHOT-standalone.jar --broker kafka-broker:9092 --topic mapped-test-json --elasticsearch http://elasticsearch:9200 --index 2017-hpc-test --index-type hpc-test

## Usage docker

    $docker run -t -i <BUILD-HASH> --broker kafka-broker:9092 --topic mapped-test-json --elasticsearch http://elasticsearch:9200 --index 2017-hpc-test --index-type hpc-test


## License

Copyright © 2017 Sojoner

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
