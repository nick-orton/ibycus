(ns ibycus.vocab
  (:require [techne.bag :as bag]
            [clojure.contrib.math :as math]))

(defprotocol Vocab
  (add-all [self words])
  (start [self])
  (words [self])
  (next-word [self words]))

(defn acc 
  [initials]
  (let [ks (ref initials)]
       (fn [bag-map v]
         (let [ks* (deref ks)
               bag (get bag-map ks* (bag/create))]
              (dosync 
                (ref-set ks (conj (vec (rest ks*)) v))
                (assoc bag-map ks* (bag/put bag v)))))))

(deftype BagVocab [bag-map size]
  Vocab
    (add-all [_ words]
      (let [init (take size words)
            rst (take 5 (drop size words))]
      (BagVocab. 
        (reduce 
          (acc (take size words)) 
          bag-map
          (drop size words))
        size)))
    (start [_] (rand-nth (keys bag-map)))
    (words [_] (vals bag-map))
    (next-word
      [_ words]
      ;TODO not found case
      (let [bag (get bag-map (take-last size words))]
           (rand-nth (bag/->seq bag)))))

(defn words->vocab
   [words]
   (add-all (BagVocab. {} 3) words))
