#!/bin/bash
mv ../target/clj-kstream-elasticsearch-sink.jar .
docker build --tag "sojoner/clj-kstream-elasticsearch-sink:0.1.0" .
