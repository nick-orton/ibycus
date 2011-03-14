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

(defn- squeeze-back-periods
  [words ]
  (loop [out []
         current (first words)
         others (rest words)]
   (if (empty? others)
     out
     (let [next (first others)
           after (rest others)]
          (if (#{"." "?" "!"} next)
            (if (empty? after)
              (conj out (str current next))
              (recur (conj out (str current next)) (first after) (rest after)))
            (recur (conj out current) next after)))))) 

(defn write-poem
  [vocab size]
  (->>
    (vocab->poem-word-list vocab size)
    (squeeze-back-periods)
    (interpose " " )
    (apply str)))
