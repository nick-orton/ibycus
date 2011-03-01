(ns ibycus.vocab
  (:require [techne.bag :as bag]
            [clojure.contrib.math :as math]))

(defprotocol Vocab
  (add [self word follower])
  (words [_])
  (add-all [self words])
  (next-word [self prev]))

(defn- vocab-adding-acc [start]
  (let [prev (ref start)]
       (fn [self follower]
         (let [prev* (deref prev)]
              (dosync 
                (ref-set prev follower)
                (add self prev* follower))))))

(deftype BagVocab [state]
  Vocab
  (add
    [self word follower]
    (let [bag (get state word (bag/create))]
         (BagVocab. (assoc state word (bag/put bag follower)))))
  (add-all [self words]
    (reduce (vocab-adding-acc (first words)) 
      self
      (rest words)))
  (words [_] (keys state))
  (next-word
    [self prev]
    ;TODO not found case
    (let [bag (get state prev)
          pick (math/floor (rand (bag/total bag)))]
          (nth (bag/->seq bag) pick)
          ))
        
  Object
  (toString [_]
    (str state)))

(defn words->vocab
   [words]
   (add-all (BagVocab. {}) words))
