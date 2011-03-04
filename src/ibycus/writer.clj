(ns ibycus.writer
  (require [ibycus.vocab :as vocab]))

(defn write-poem
  [vocab size]
  (loop [i 0
         prev (vocab/start-word vocab)
         poem [prev]]
    (let [word (vocab/next-word vocab prev)
          poem* (conj poem word)]
         (if (< i size)
           (recur (inc i) word poem*)
           poem*))))
