(ns clj-kstream-elasticsearch-sink.core
  (:use [clojure.tools.logging :only (info debug error warn)])
  (:require [clojure.data.json :as json]
            [clojure.tools.cli :as cli]
            [clojure.core.async :refer [<!! >!! <! >! close! chan go-loop]]
            [clj-kstream-elasticsearch-sink.cli :as cli-def]
            [clj-kstream-elasticsearch-sink.from-kafka :as from-kafka]
            [clj-kstream-elasticsearch-sink.to-elasticsearch :as to-es])
  (:gen-class))

;; State
(def state (atom {}))

(defn- start-strange-loop []
  (info "Started strange loop")
  (while true
    (and
      (<!! (:consumer_sentinal @state))
      (<!! (:es_sentinal @state)))))

(defn- stop-strange-loop []
  (info "Stopping Strange Loop")
  (Thread. #(do
              (close! (:consumer_sentinal @state))
              (close! (:es_sentinal @state)))))

(defn- listen-for-shutdown []
  (.addShutdownHook
    (Runtime/getRuntime)
    (stop-strange-loop)))

(defn bootstrap []
  (swap! state assoc
         :from_kafa (chan)
         :consumer (from-kafka/create-consumer @state)
         :es_connection (to-es/create-es-connection (:elasticsearch @state)))
  (swap! state assoc :consumer_sentinal (from-kafka/start-consuming @state))
  (swap! state assoc :es_sentinal (to-es/start-indexing @state)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-def/cli-options)]
    (cond
      (:help options) (cli-def/exit 0 (cli-def/usage summary))
      (not= (count (keys options)) 5) (cli-def/exit 1 (cli-def/usage summary))
      errors (cli-def/exit 1 (cli-def/error-msg errors)))
    (swap! state assoc
           :es_type (:index-type options)
           :es_index (:index options)
           :elasticsearch (:elasticsearch options)
           :broker (:broker options)
           :topic (:topic options)
           :consumer-group-id "clj-kstream-elasticsearch-sink"
           :consumer_sentinal (chan)
           :es_sentinal (chan))
    (bootstrap)
    (listen-for-shutdown)
    (start-strange-loop)))
