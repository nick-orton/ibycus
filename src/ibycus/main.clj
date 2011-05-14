(ns ibycus.main
  (:use [ibycus.writer :only [create-poet write-poem write-sentence]]) 
  (:use [ibycus.reader :only [create-from-dir]]))

(def vocab (create-from-dir "vocab"))
(def ibycus (create-poet vocab))

(defn write
  ([size] (write-poem ibycus size))
  ([] (write-sentence ibycus)))
