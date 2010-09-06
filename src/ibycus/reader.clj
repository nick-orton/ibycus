 (ns ibycus.reader
   (:use [techne.bag]))

(defn- add-to-vocab
  [vocab word follower]
  (let [bag (get vocab word {})]
       (assoc vocab word (put bag follower))))

(defn words->vocab
  [words vocab]
  (let [prev (ref (first words))]
    (reduce 
      (fn [vocab follower]
        (dosync
          (let [word (deref prev)]
            (ref-set prev follower)
            (add-to-vocab vocab word follower))))
      {}
      (rest words))))


