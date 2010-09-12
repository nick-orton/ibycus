(ns ibycus.vocab
  (:require [techne.bag :as bag]))

(defn add
  [vocab word follower]
  (let [bag (get vocab word (bag/create))]
       (assoc vocab word (bag/put bag follower))))


