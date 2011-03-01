 (ns ibycus.reader
   (:require [clojure.contrib.str-utils2 :as str]
             [techne.files :as files]
             [ibycus.vocab :as vocab]))

(defn ref-get+set 
  ;TODO: no
  "ref-set and return old value"
  [r value]
  (let [old (deref r)]
       (ref-set r value)
       old))

(defn words->vocab
  [words]
    (reduce 
      (let [prev (ref (first words))]
        (fn [vocab follower]
          (dosync 
            (vocab/add vocab (ref-get+set prev follower) follower))))
      (vocab/create)
      (rest words)))

(defn words->vocab*
  [words vocab ]
  (print vocab))

(defn string->words
  [s]
  (filter #(not (empty? %)) (str/split s #"[\s+\d*\(\)]")))  

(defn file->vocab
  [file]
  (->
    (files/file->string file)
    (string->words)
    (words->vocab )))
