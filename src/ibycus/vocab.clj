(ns ibycus.vocab
  (:require [techne.bag :as bag]
            [clojure.contrib.math :as math]))

(defprotocol Vocab
  (words [_])
  (add-all [self words])
  (start-word [_])
  (next-word [self prev]))

(defprotocol Addable
  (add [self key val]))

(defn- vocab-adding-acc 
  [start]
  (let [prev (ref start)]
       (fn [self follower]
         (let [prev* (deref prev)]
              (dosync 
                (ref-set prev follower)
                (add self prev* follower))))))

(deftype BagVocab [state]
  Addable
    (add
      [self word follower]
      (let [bag (get state word (bag/create))]
           (BagVocab. (assoc state word (bag/put bag follower)))))

  Vocab
    (add-all [self words]
      (reduce (vocab-adding-acc (first words)) 
        self
        (rest words)))
    (words [_] (keys state))
    (start-word [_] (rand-nth (keys state)))
    (next-word
      [self prev]
      ;TODO not found case
      (let [bag (get state prev)]
           (rand-nth (bag/->seq bag))))
        
  Object
    (toString [_]
      (str state)))

(defn words->vocab
   [words]
   (add-all (BagVocab. {}) words))
