(ns ibycus.vocab
  (:require [techne.bag :as bag]))

(defprotocol Vocab
  (add [self word follower])
  (add-all [self words]))

(defn- ref-get+set 
  ;TODO: no
  "ref-set and return old value"
  [r value]
  (let [old (deref r)]
       (ref-set r value)
       old))

(defn- vocab-adding-acc [start]
  (let [prev (ref start)]
       (fn [self follower]
         (dosync 
           (add self (ref-get+set prev follower) follower)))))

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
        
  Object
  (toString [_]
    (str state)))

(defn words->vocab
   [words]
   (add-all (BagVocab. {}) words))
