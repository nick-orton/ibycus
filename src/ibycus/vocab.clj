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
  (add [self ks v])
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
      (let [bag (leaf chain (take-last (:size chain) words))]
           (rand-nth (bag/->seq bag)))))


(defrecord BagChain1 []
  Chain
    (start-chain [self] [(rand-nth (keys self))])
    (next-link [self key]
      (get self key (bag/create)))
    
    (leaf [self words]
      (get self (last words)))
    (add [self ks v] 
         (let [k (first ks)
               bag (next-link self k)]
              (assoc self k (bag/put bag v))))
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

(defrecord BagChain2 [size create-link-fun]
  Chain
    (start-chain [self] 
      (let [link (rand-nth (keys self))]
           (vec (cons link (start-chain (get self link))) )))
    (next-link [self key]
      (get self key (create-link-fun)))
    (leaf [self words]
      (let [link (next-link self (first words))]
           (leaf link (rest words))))
    (add [self ks v]
         (let [k (first ks)
               chain (next-link self k)]
               (assoc self k (add chain (rest ks) v))))
    (acc 
      [self keys]
      (let [ks (ref (take size keys))]
           (fn [self follower]
             (let [ks* (deref ks)]
                  (dosync 
                    (ref-set ks (conj (vec (rest ks*)) follower))
                    (add self ks* follower)))))))

(defn words->vocab
   [words]
   (add-all (BagVocab. (BagChain2. 2 make-bc1)) words))
