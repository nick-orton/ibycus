(ns ibycus.main
  (:use [ibycus.writer :only [create-poet write-poem write-sentence]]) 
  (:use [ibycus.reader :only [create-from-dir]]))

(defn build-ibycus
  []
  (let [vocab (create-from-dir "vocab")]
       (create-poet vocab)))
