(ns ibycus.writer
  (:require  [clojure.contrib.str-utils2 :as str-utils2]
             [ibycus.reader :as reader]
             [ibycus.vocab :as vocab]))

(def puncts #{"." "?" "!"})

(defn- vocab->poem-word-list
  [vocab size]
  (loop [i 0
         poem (vocab/start vocab)]
    (let [word (vocab/next-word vocab poem)
          poem* (conj poem word)]
         (if (< i size)
           (recur (inc i) poem*)
           poem*))))

(defn- vocab->sentence 
  [vocab]
  (loop [poem (vocab/start vocab)]
    (let [word (vocab/next-word vocab poem)
          poem* (conj poem word)]
         (if (puncts word)
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
          (if (puncts next)
            (if (empty? after)
              (conj out (str current next))
              (recur (conj out (str current next)) (str-utils2/capitalize (first after)) (rest after)))
            (recur (conj out current) next after)))))) 

(defn- words->string
  [words]
  (->>
    (interpose " " words)
    (apply str)
    (str-utils2/capitalize)))

(defn- write-poem
  [size]
  (->>
    (vocab->poem-word-list reader/v size)
    (attach-punctuation-marks-to-the-word-before)
    (filter #(not (= "." %)))
    (words->string)))

(defn- write-sentence
  []
  (->>
    (vocab->sentence reader/v)
    (words->string)))

(defn write
  ([size] (write-poem size))
  ([] (write-sentence)))
