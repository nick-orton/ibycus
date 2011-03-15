(ns ibycus.writer
  (:require  [clojure.contrib.str-utils2 :as str-utils2]
             [ibycus.vocab :as vocab]))

(defn- vocab->poem-word-list
  [vocab size]
  (loop [i 0
         poem (vocab/start vocab)]
    (let [word (vocab/next-word vocab poem)
          poem* (conj poem word)]
         (if (< i size)
           (recur (inc i) poem*)
           poem*))))

(defn- vocab->sentance 
  [vocab]
  (loop [poem (vocab/start vocab)]
    (let [word (vocab/next-word vocab poem)
          poem* (conj poem word)]
         (if (#{"." "?" "!"} word)
           poem*
           (recur poem*)))))
 

(defn- attach-punctuation-marks-to-the-word-before
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
              (recur (conj out (str current next)) (str-utils2/capitalize (first after)) (rest after)))
            (recur (conj out current) next after)))))) 

(defn write-poem
  [vocab size]
  (->>
    (vocab->poem-word-list vocab size)
    (attach-punctuation-marks-to-the-word-before)
    (filter #(not (= "." %)))
    (interpose " " )
    (apply str)))

(defn write-sentance
  [vocab]
  (->>
    (vocab->sentance vocab)
    (interpose " " )
    (apply str)
    (str-utils2/capitalize)))
