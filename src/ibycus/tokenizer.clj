 (ns ibycus.tokenizer
   (:import (java.io File))
   (:require [clojure.contrib.str-utils2 :as str-utils2]
             [clojure.contrib.str-utils :as str-utils]
             [clojure.contrib.string :as contrib-str]
             ))

(defn- tokenize [string]
  (str-utils2/split string #"[\s+\d*\(\),:\"\]\[*;#]"))

(def forbidden-words 
  #{"ll" "ii" "iii" "iv" "v" "vii" "viii" "ix" "x" "xi" "xii" "xiii" "xiv"
    "xv" "xvi" "xvii" "xviii" "xix" "xx" "xxi" "xxii" "xxiii" "xxiv" "xxv" 
    "xxvi" "xxvii" "xxviii" "xxix" "xxx" "fragment" "scholiast" "--" "'" 
    "_" "-" "l" })
                        
(defn- clean-word
  [word]
  (str-utils/re-gsub #"--|\.\.+" "" word))

(defn- extract-period-ends [words]
 (flatten (map #(str-utils/re-partition #"\.|\?|!" %) words)))

(defn string->words
  [s]
  (->> 
    (tokenize s)
    (filter #(not (empty? %)))
    (map clean-word)
    (extract-period-ends)
    (filter #(nil? (forbidden-words %)))
    (map contrib-str/lower-case)))
