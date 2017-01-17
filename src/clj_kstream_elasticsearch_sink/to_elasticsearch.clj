(ns clj-kstream-elasticsearch-sink.to-elasticsearch
  (:use [clojure.tools.logging :only (debug info error)])
  (:require
    [clojure.core.async :refer [<!! >!! <! >! close! chan go-loop]]
    [clojure.data.json :as json]
    [clojurewerkz.elastisch.query :as q]
    [clojurewerkz.elastisch.rest :as esr]
    [clojurewerkz.elastisch.rest.bulk :as bulk]
    [clojurewerkz.elastisch.rest.document :as esd]
    [clojurewerkz.elastisch.rest.response :refer [hits-from]]
    [clojure.tools.cli :as cli]
    [clojure.string :as string])
  (:gen-class)
  (:import (java.util UUID)))


(defn create-es-connection [url]
  (esr/connect url))

(defn index [msg conf]
  (try
    (debug "Try indexing" (:key msg) (:value msg))
    (esd/put (:es_connection conf)
             (:es_index conf)
             (:es_type conf)
             (if (nil? (:key msg)) (str (UUID/randomUUID)) (:key msg))
             (json/read-str (:value msg)))
    (catch Exception e
      (error "Failed indexing: " e))))

(defn start-indexing [conf]
  (go-loop []
    (let [response (index (<! (:from_kafa conf)) conf)]
      (when (not-empty response)
        (debug "Response: " response)))
    (recur)))