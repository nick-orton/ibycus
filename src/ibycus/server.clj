(ns ibycus.server
    (:use [clojure.contrib server-socket])
    (:require [clojure.contrib.duck-streams :as streams])
    (:gen-class))

(defn- cleanup )

(defn- client-loop [in out]
  (binding [*in* (streams/reader in)
            *out* (streams/writer out)]
      (try 
        (loop [input (read-line) roomname "default"]
             (when input 
               (print (str "echo>>" input))
             ))
        (finally (cleanup)))))

(defn -main
  [& args] 
  (let [port (0 args)]
       (println "Launching Ibycus on port" port)
       (def server-instance (create-server (Integer. port) client-loop))))

;duck-streams
