 (ns ibycus.reader
   (:use [techne.bag])
   (:require [clojure.contrib.str-utils2 :as str]
             [techne.files :as files]
             [ibycus.vocab :as vocab]))

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
            (vocab/add vocab (ref-set? prev follower) follower))))
      {}
      (rest words)))

(defn string->words
  [s]
  (filter #(not (empty? %)) (str/split s #"[\s+\d*\(\)]")))  

(defn file->vocab
  [file]
  (->
    (files/file->string file)
    (string->words)
    (words->vocab {})))
