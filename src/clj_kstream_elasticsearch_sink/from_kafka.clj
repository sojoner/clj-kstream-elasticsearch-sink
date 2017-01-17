(ns clj-kstream-elasticsearch-sink.from-kafka
  (:require
    [clojure.tools.logging :as log]
    [franzy.serialization.deserializers :as desy]
    [franzy.clients.consumer.client :as consumer]
    [franzy.common.models.types :as mt]
    [franzy.clients.consumer.callbacks :as callbacks]
    [franzy.clients.consumer.defaults :as cd]
    [franzy.clients.consumer.protocols :refer :all]
    [clojure.core.async :refer [<! >! >!! <!! go-loop]]
    [taoensso.timbre :as timbre])
  (:gen-class))


(defn create-consumer [state]
  (let [cc {:bootstrap.servers  (:broker state)
            :group.id           (:consumer-group-id state)
            :auto.offset.reset  :earliest
            :enable.auto.commit true}
        rebalance-listener (callbacks/consumer-rebalance-listener (fn [topic-partitions]
                                                                    (log/info "topic partitions assigned:" topic-partitions))
                                                                  (fn [topic-partitions]
                                                                    (log/info "topic partitions revoked:" topic-partitions)))
        options (cd/make-default-consumer-options {:rebalance-listener-callback rebalance-listener})]
    (consumer/make-consumer cc (desy/string-deserializer) (desy/string-deserializer) options)))

(defn start-consuming [state]
  (subscribe-to-partitions! (:consumer state) [(:topic state)])
  (go-loop []
    (log/debug "Partitions subscribed to:" (partition-subscriptions (:consumer state)))
    (let [poll (poll! (:consumer state))]
      (doseq [item poll]
        (log/info "Consumed: " (:key item) (:value item))
        (>! (:from_kafa state) {:key (:key item) :value (:value item)})))
    (recur)))