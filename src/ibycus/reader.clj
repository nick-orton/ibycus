 (ns ibycus.reader
   (:use [techne.bag])
   (:require [clojure.contrib.str-utils2 :as str]
             [techne.files :as files]))

(defn- vocab+
  [vocab word follower]
  (let [bag (get vocab word {})]
       (assoc vocab word (put bag follower))))

(defn ref-set? 
  ;TODO: no
  "ref-set and return old value"
  [r value]
  (let [old (deref r)]
       (ref-set r value)
       old))

(defn words->vocab
  [words vocab]
    (reduce 
      (let [prev (ref (first words))]
        (fn [vocab follower]
          (dosync 
            (vocab+ vocab (ref-set? prev follower) follower))))
      {}
      (rest words)))

(defn string->words
  [s]
  (str/split s #" "))  

(defn file->vocab
  [file]
  (->
    (files/file->string file)
    (string->words)
    (words->vocab {})))
