 (ns ibycus.reader
   (:import (java.io File))
   (:require [clojure.contrib.str-utils2 :as str-utils2]
             [clojure.contrib.str-utils :as str-utils]
             [techne.files :as files]
             [clojure.contrib.string :as contrib-str]
             [ibycus.tokenizer :as tokenizer]
             [ibycus.vocab :as vocab]))

(defn- file->words
  [file]
  (-> 
    (files/file->string file)
    (tokenizer/string->words)))

(defn files->vocab
  [files]
  (->
    (map #(file->words %) files)
    (flatten)
    (vocab/words->vocab )))


(def vocab-files (files/dir->files "vocab"))

(def v (files->vocab vocab-files))

