(ns clj-kstream-elasticsearch-sink.cli
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-b" "--broker comma seperated" "The kafka brokers hosts"
    :validate [#(string? %1) "Must be a sth like HOST:PORT,HOST:PORT"]]
   ["-z" "--zookeeper comma seperated" "The zookeeper hosts"
    :validate [#(string? %1) "Must be a sth like HOST:PORT,HOST:PORT"]]
   ["-t" "--topic the topic name" "The input topic name"
    :validate [#(string? %1) "Must be a sth like NAME"]]
   ["-e" "--elasticsearch host:port" "The elasticsearch host with the related http:// port, sth like host:port"
    :validate [#(string? %1) "Must be a sth like host:port"]]
   ["-i" "--index the elasticsearch index name" "The elasticsearch index name"
    :validate [#(string? %1) "Must be a sth like NAME"]]
   ["-j" "--index-type the elasticsearch index name" "The elasticsearch index name"
    :validate [#(string? %1) "Must be a sth like NAME"]]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (clojure.string/join \newline errors)))

(defn usage [options-summary]
  (->> ["This is my program. There are many like it, but this one is mine."
        ""
        "Usage: program-name [options] action"
        ""
        "Options:"
        options-summary]
       (clojure.string/join \newline)))

(defn exit [status _]
  (System/exit status))