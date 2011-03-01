 (ns ibycus.reader
   (:require [clojure.contrib.str-utils2 :as str]
             [techne.files :as files]
             [ibycus.vocab :as vocab]))

(defn string->words
  [s]
  (filter #(not (empty? %)) (str/split s #"[\s+\d*\(\)]")))  

(defn file->vocab
  [file]
  (->
    (files/file->string file)
    (string->words)
    (vocab/words->vocab )))
