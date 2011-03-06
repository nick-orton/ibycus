(ns ibycus.vocab
  (:require [techne.bag :as bag]
            [clojure.contrib.math :as math]))

(defprotocol Vocab
  (add-all [self words])
  (start [self])
  (next-word [self words]))

(defprotocol Chain
  (acc [self start])
  (leaf [self prev]))


(deftype BagVocab [chain]
  Vocab
    (add-all [_ words]
      (BagVocab. (reduce (acc chain (first words)) 
        chain
        (rest words))))
    (start [_] (rand-nth (keys chain)))
    (next-word
      [_ words]
      ;TODO not found case
      (let [bag (leaf chain words)]
           (rand-nth (bag/->seq bag)))))


(defrecord BagChain1 []
  Chain
    (leaf [self words]
      (get self (last words)))
    (acc 
      [self start]
      (let [prev (ref start)]
           (fn [self follower]
             (let [prev* (deref prev)]
                  (dosync 
                    (ref-set prev follower)
                    (let [bag (get self prev* (bag/create))]
                         (assoc self prev* (bag/put bag follower)))))))))

(defn words->vocab
   [words]
   (add-all (BagVocab. (BagChain1.)) words))
