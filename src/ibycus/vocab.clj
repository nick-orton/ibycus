(ns ibycus.vocab
  (:require [techne.bag :as bag]
            [clojure.contrib.math :as math]))

(defprotocol Vocab
  (add-all [self words-to-add])
  (start [self])
  (next-word [self prev-words]))

(defn- drop-first-add-to-end [es e]
  ; TODO promote to library
  (conj (vec (rest es)) e))

(defn- make-acc 
  [initials]
  (let [ks (ref initials)]
       (fn [bag-map v]
         (let [ks* (deref ks)
               bag (get bag-map ks* (bag/create))]
              (dosync 
                (ref-set ks (drop-first-add-to-end ks* v))
                (assoc bag-map ks* (bag/put bag v)))))))

(deftype BagVocab [bag-map size]
  Vocab
    (add-all [_ words]
      (BagVocab. 
        (reduce (make-acc (take size words)) bag-map (drop size words))
        size))
    (start [_] (rand-nth (keys bag-map)))
    (next-word
      [_ prevs]
      ;TODO not found case
      (let [bag (get bag-map (take-last size prevs))]
           (rand-nth (bag/->seq bag)))))

(defn words->vocab
   [words]
   (add-all (BagVocab. {} 3) words))
