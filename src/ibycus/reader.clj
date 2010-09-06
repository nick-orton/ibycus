 (ns ibycus.reader
   (:use [techne.bag]))

(defn- add-to-vocab
  [vocab word follower]
  (let [bag (get vocab word {})]
       (assoc vocab word (put bag follower))))

(defn- into-vocab
  [first-word]
  (let [prev (ref first-word)]
    (fn [vocab follower]
      (dosync
        (let [word (deref prev)]
          (ref-set prev follower)
          (add-to-vocab vocab word follower))))))

(defn words->vocab
  [words vocab]
  (reduce 
    (into-vocab (first words)) {}
    (rest words)))


