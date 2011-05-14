(ns ibycus.writer
  (:use [clojure.contrib.str-utils2 :only [capitalize]])
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

(defprotocol Poet
  "A poet can write poems of a given word count, or a poetic sentence of 
   arbitrary length"
  (write-poem [self size])
  (write-sentence [self]))

(deftype VocabUsingPoet [vocab]
  Poet
  (write-poem
    [_ size]
    (->>
      (vocab->poem-word-list vocab size)
      (attach-punctuation-marks-to-the-word-before)
      (filter #(not (= "." %)))
      (words->string)))

  (write-sentence
    [_]
    (->>
      (vocab->sentence vocab)
      (words->string))))

(defn create-poet [vocab] (VocabUsingPoet. vocab))
