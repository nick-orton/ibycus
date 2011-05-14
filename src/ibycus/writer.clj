(ns ibycus.writer
  (:gen-class)
  (:use [clojure.contrib.str-utils2 :only [capitalize]])
  (:use [ibycus.reader :only [v]])
  (:use [ibycus.vocab :only [start next-word]]))


;TODO no poem should start with punctuation
(def puncts #{"." "?" "!"})

(defn- vocab->poem-word-list
  [vocab size]
  (loop [i 0
         poem (start vocab)]
    (let [word (next-word vocab poem)
          poem* (conj poem word)]
         (if (< i size)
             (recur (inc i) poem*)
             poem*))))

(defn- vocab->sentence 
  [vocab]
  (loop [poem (start vocab)]
    (let [word (next-word vocab poem)
          poem* (conj poem word)]
         (if (puncts word)
           (conj (vec (butlast poem)) (str (last poem) word))
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
                  (recur (conj out (str current next)) 
                         (capitalize (first after)) 
                         (rest after)))
              (recur (conj out current) next after)))))) 

(defn- words->string
  [words]
  (->>
    (interpose " " words)
    (apply str)
    (capitalize)))

;TODO move write-poem and write-sentance into a Protocol Poet.
;TODO create a poet that takes a vocabulary
;move reader's v fun into a seperate file that creates the vocabulary and poet
;  and does the write method
(defn- write-poem
  [size]
  (->>
    (vocab->poem-word-list v size)
    (attach-punctuation-marks-to-the-word-before)
    (filter #(not (= "." %)))
    (words->string)))

(defn- write-sentence
  []
  (->>
    (vocab->sentence v)
    (words->string)))

(defn write
  ([size] (write-poem size))
  ([] (write-sentence)))

(declare main) 

(defn -main [& args] (write args))
