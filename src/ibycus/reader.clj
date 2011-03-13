 (ns ibycus.reader
   (:require [clojure.contrib.str-utils2 :as str-utils]
             [techne.files :as files]
             [clojure.contrib.string :as contrib-str]
             [ibycus.vocab :as vocab]))

(defn string->words
  [s]
  (->> 
    (str-utils/split s #"[\s+\d*\(\),(--):\"\]\[\.*;?'!]")
    (filter #(not (empty? %)))
    (filter #(nil? (#{"ll"} %)))
    (map contrib-str/lower-case)))

(defn file->vocab
  [file]
  (->
    (files/file->string file)
    (string->words)
    (vocab/words->vocab )))

(def v (file->vocab "vocab/hesiod.txt"))
