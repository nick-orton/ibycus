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
       (fn [bag v]
         (let [ks* (deref ks)
               bag (get bag ks* (create))]
              (dosync 
                (ref-set ks (drop-first-add-to-end ks* v))
                (assoc bag ks* (put bag v)))))))

;TODO serialize with print-dup
;http://clojuredocs.org/clojure_core/clojure.core/*print-dup*
; - depends on bag being serializable
(deftype BagVocab [bag size]
  Vocab
    (add-all [_ words]
      (BagVocab. 
        (reduce (make-acc (take size words)) bag (drop size words))
        size))
    (start [_] (rand-nth (keys bag)))
    (next-word
      [_ prevs]
      ;TODO not found case
      (let [bag (get bag (take-last size prevs))]
           (rand-nth (->seq bag)))))

(defn words->vocab
   [words]
   (add-all (BagVocab. {} 3) words))
