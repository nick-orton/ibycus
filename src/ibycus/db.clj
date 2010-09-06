(ns ibycus.db
    (:use [clojure.contrib.sql :as sql]))


(let [db-name  "ibycus"]
  (def db {:classname   "org.h2.Driver" 
           :subprotocol "h2"
           :subname (str "file:~/.h2data/" db-name)
           :user     "sa"
           :password ""}))

(defn create-vocab
  "Create a table"
  []
  (sql/with-connection db
    (sql/create-table :vocab
      [:word "varchar(32)" "PRIMARY KEY"]
      [:follower "varchar(32)"]
      [:freq :int])))

(defn drop-vocab
  "Drop a table"
  []
  (sql/with-connection db
    (try
      (sql/drop-table :vocab)
      (catch Exception _))))
