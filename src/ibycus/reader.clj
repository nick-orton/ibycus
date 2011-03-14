 (ns ibycus.reader
   (:import (java.io File))
   (:require [clojure.contrib.str-utils2 :as str-utils]
             [techne.files :as files]
             [clojure.contrib.string :as contrib-str]
             [ibycus.vocab :as vocab]))

(defn- tokenize [string]
  (str-utils/split string #"[\s+\d*\(\),:\"\]\[\.*;?'!]"))

(defn string->words
  [s]
  (->> 
    (tokenize s)
    (filter #(not (empty? %)))
    (filter #(nil? (#{"ll"} %)))
    (map contrib-str/lower-case)))

(defn file->vocab
  [file]
  (->
    (files/file->string file)
    (string->words)
    (vocab/words->vocab )))

(def vocab-files (files/dir->files "vocab"))

(def v (file->vocab (first vocab-files)))

