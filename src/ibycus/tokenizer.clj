 (ns ibycus.tokenizer
   (:import (java.io File))
   (:use [clojure.contrib.str-utils2 :only [split]])
   (:use [clojure.contrib.str-utils :only [re-gsub re-partition]])
   (:use [clojure.contrib.string :only [lower-case]]))

(defn- tokenize [string]
  (split string #"[\s+\d*\(\),:\"\]\[*;#]"))

(def forbidden-words 
  #{"ll" "ii" "iii" "iv" "v" "vii" "viii" "ix" "x" "xi" "xii" "xiii" "xiv"
    "xv" "xvi" "xvii" "xviii" "xix" "xx" "xxi" "xxii" "xxiii" "xxiv" "xxv" 
    "xxvi" "xxvii" "xxviii" "xxix" "xxx" "fragment" "scholiast" "--" "'" 
    "_" "-" "l" })
                        
(defn- clean-word
  [word]
  (re-gsub #"--|\.\.+" "" word))

(defn- extract-period-ends [words]
 (flatten (map #(re-partition #"\.|\?|!" %) words)))

(defn string->words
  [s]
  (->> 
    (tokenize s)
    (filter #(not (empty? %)))
    (map clean-word)
    (extract-period-ends)
    (filter #(nil? (forbidden-words %)))
    (map lower-case)))
