(ns ibycus.core)

(defn next-word [prev vocab]
  "a")

(defn write-poem
  [num-chars vocab]
  (loop [i 0
         prev ::START-TOKEN
         poem []]
    (let [word (next-word prev vocab)
          poem+ (conj poem word)]
      (if (< i num-chars)
          (recur (inc i) word poem+)
          poem+))))
