(ns ibycus.vocab
  (:require [techne.bag :as bag]))

(defprotocol Vocab
  (add [vocab word follower]))

(declare create)

(deftype BagVocab [state]
 Vocab
 (add
   [vocab word follower]
   (let [bag (get state word (bag/create))]
        (create (assoc state word (bag/put bag follower)))))
         
  Object
  (toString [self]
    (str state))
  )

 
(defn create 
  ([] 
   (BagVocab. {}))
  ([state] 
   (BagVocab. state)))
