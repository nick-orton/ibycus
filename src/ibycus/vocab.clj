(ns ibycus.vocab
  (:require [techne.bag :as bag]))

(defprotocol Vocab
  (add [vocab word follower])
  (words->vocab [words]))

(declare create)

(deftype BagVocab [state]
 Vocab
 (add
   [vocab word follower]
   (let [bag (get state word (bag/create))]
        (create (assoc state word (bag/put bag follower)))))
        
  Object
  (toString [self]
    (str state)))

(defn- ref-get+set 
  ;TODO: no
  "ref-set and return old value"
  [r value]
  (let [old (deref r)]
       (ref-set r value)
       old))

(defn words->vocab
   [words]
   (reduce 
     (let [prev (ref (first words))]
          (fn [vocab follower]
            (dosync 
              (add vocab (ref-get+set prev follower) follower))))
     (create)
     (rest words)))

 
(defn create 
  ([] 
   (BagVocab. {}))
  ([state] 
   (BagVocab. state)))
