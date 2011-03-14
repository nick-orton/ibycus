 (ns ibycus.reader
   (:import (java.io File))
   (:require [clojure.contrib.str-utils2 :as str-utils2]
             [clojure.contrib.str-utils :as str-utils]
             [techne.files :as files]
             [clojure.contrib.string :as contrib-str]
             [ibycus.vocab :as vocab]))

(defn- tokenize [string]
  (str-utils2/split string #"[\s+\d*\(\),:\"\]\[\.*;?!#]"))

(def forbidden-words 
  #{"ll" "ii" "iii" "iv" "v" "vii" "viii" "ix" "x" "xi" "xii" "xiii" "xiv"
    "xv" "xvi" "xvii" "xviii" "xix" "xx" "xxi" "xxii" "xxiii" "xxiv" "xxv" 
    "xxvi" "xxvii" "xxviii" "xxix" "xxx" "fragment" "scholiast" "--" "'" 
    "_" "-" })
                        
(defn- clean-word
  [word]
  (str-utils/re-gsub #"--" "" word))

(defn string->words
  [s]
  (->> 
    (tokenize s)
    (filter #(not (empty? %)))
    (map clean-word)
    (filter #(nil? (forbidden-words %)))
    (map contrib-str/lower-case)))

(defn- file->words
  [file]
  (-> 
    (files/file->string file)
    (string->words)))

(defn files->vocab
  [files]
  (->
    (map #(file->words %) files)
    (flatten)
    (vocab/words->vocab )))


(def vocab-files (files/dir->files "vocab"))

(def v (files->vocab vocab-files))

