(ns ibycus.writer
  (require [ibycus.vocab :as vocab]))

(defn write-poem
  [vocab size]
  (loop [i 0
         prev (vocab/start vocab)
         poem [prev]]
    (let [word (vocab/next-word vocab poem)
          poem* (conj poem word)]
         (if (< i size)
           (recur (inc i) word poem*)
           poem*))))
