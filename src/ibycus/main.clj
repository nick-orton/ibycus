(ns ibycus.main
  (:use [ibycus.writer :only [ibycus write-poem write-sentence]]) 
  (:use [ibycus.reader :only [create-from-dir]]))


(def ib (ibycus (create-from-dir "vocab")))

(defn write
  ([size] (write-poem ib size))
  ([] (write-sentence ib)))
