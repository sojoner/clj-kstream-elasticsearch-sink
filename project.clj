(defproject clj-kstream-elasticsearch-sink "0.1.0"
  :description "A Clojure tool designed to read from a kafka topic and index the\nitems into elasticsearch"
  :url "git@github.com:sojoner/clj-kstream-elasticsearch-sink.git"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.cli "0.3.5"]
                 ; elasticsearch
                 [clojurewerkz/elastisch "2.2.1"]
                 ; kafka
                 [ymilky/franzy "0.0.2-SNAPSHOT"]
                 [ymilky/franzy-nippy "0.0.1"]
                 [ymilky/franzy-fressian "0.0.1"]
                 [ymilky/franzy-json "0.0.1"]
                 ; logging
                 [com.taoensso/timbre "4.4.0"]
                 [org.clojure/tools.logging "0.2.6"]
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :aot :all
  :main clj-kstream-elasticsearch-sink.core
  :profiles {:uberjar {:aot :all}}
  ;; As above, but for uberjar.
  :uberjar-name "clj-kstream-elasticsearch-sink.jar"
  :jvm-opts ["-Xmx2g" "-server"])
