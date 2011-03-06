(ns ibycus.vocab
  (:require [techne.bag :as bag]
            [clojure.contrib.math :as math]))

(defprotocol Vocab
  (add-all [self words])
  (start [self])
  (words [self])
  (next-word [self words]))

(defprotocol Chain
  (acc [self start])
  (start-chain [chain])
  (next-link [self key])
  (leaf [self prev]))


(deftype BagVocab [chain]
  Vocab
    (add-all [_ words]
      (BagVocab. (reduce (acc chain words) 
        chain
        (rest words))))
    (start [_] (start-chain chain))
    (words [_] (vals chain))
    (next-word
      [_ words]
      ;TODO not found case
      (let [bag (leaf chain words)]
           (rand-nth (bag/->seq bag)))))


(defrecord BagChain1 []
  Chain
    (start-chain [self] [(rand-nth (keys self))])
    (next-link [self key]
      (get self key (bag/create)))
    
    (leaf [self words]
      (get self (last words)))
    (acc 
      [self words]
      (let [prev (ref (first words))]
           (fn [self follower]
             (let [prev* (deref prev)]
                  (dosync 
                    (ref-set prev follower)
                    (let [bag (next-link self prev* )]
                         (assoc self prev* (bag/put bag follower)))))))))

(defn make-bc1 [] (BagChain1.))

(defrecord BagChain2 []
  Chain
    (start-chain [self] 
      (let [link (rand-nth (keys self))]
           (vec (cons link (start-chain (get self link))) )))
    (next-link [self key]
      (get self key (make-bc1)))
    (leaf [self words]
      (let [key (last (butlast words))
            chain1 (next-link self key)]
           (get chain1 (last words))))
    (acc 
      [self words]
      (let [prev1 (ref (second words))
            prev2 (ref (first words))]
           (fn [self follower]
             (let [prev1* (deref prev1)
                   prev2* (deref prev2)]
                  (dosync 
                    (ref-set prev2 prev1*)
                    (ref-set prev1 follower)
                    (let [chain1 (next-link self prev2*)
                          bag (next-link chain1 prev1*)]
                         (assoc self prev2* (assoc chain1 prev1* (bag/put bag follower))))))))))

(defn words->vocab
   [words]
   (add-all (BagVocab. (BagChain2.)) words))
