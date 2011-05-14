(ns ibycus.vocab
  (:use [techne.bag :only [put create ->seq]]))

(defprotocol Vocab
  (add-all [self words-to-add])
  (start [self])
  (next-word [self prev-words]))

(defn- drop-first-add-to-end [es e]
  (conj (vec (rest es)) e))

(defn- make-acc 
  [initials]
  (let [ks (ref initials)]
       (fn [data v]
         (let [ks* (deref ks)
               bag (get data ks* (create))]
              (dosync 
                (ref-set ks (drop-first-add-to-end ks* v))
                (assoc data ks* (put bag v)))))))

;TODO serialize with print-dup
;http://clojuredocs.org/clojure_core/clojure.core/*print-dup*
; - depends on bag being serializable
(deftype BagVocab [data size]
  Vocab
    (add-all [_ words]
      (BagVocab. 
        (reduce (make-acc (take size words)) data (drop size words))
        size))
    (start [_] (rand-nth (keys data)))
    (next-word
      [_ prevs]
      ;TODO not found case
      (let [bag (get data (take-last size prevs))]
           (rand-nth (->seq bag)))))

(defn words->vocab
   [words]
   (add-all (BagVocab. {} 3) words))
