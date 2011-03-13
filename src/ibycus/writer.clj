(ns ibycus.writer
  (require [ibycus.vocab :as vocab]))

(defn- vocab->poem-word-list
  [vocab size]
  (loop [i 0
         poem (vocab/start vocab)]
    (let [word (vocab/next-word vocab poem)
          poem* (conj poem word)]
         (if (< i size)
           (recur (inc i) poem*)
           poem*))))

(defn write-poem
  [vocab size]
  (apply str (interpose " " (vocab->poem-word-list vocab size))))
