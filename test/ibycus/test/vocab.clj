(ns ibycus.test.reader
  (:use [ibycus.vocab] :reload-all)
  (:use [clojure.test])
  (:require [techne.bag :as bag]))

(deftest test-words->vocab
  (is (= {:a (bag/create {:a 1})} (words->vocab [:a :a] {})))
  (is (= {:a (bag/create {:a 2})} (words->vocab [:a :a :a] {})))
  (is (= {:a (bag/create {:a 1 :b 1})} (words->vocab [:a :a :b] {})))
  (is (= {:a (bag/create {:b 1}) :b (bag/create {:b 1})} (words->vocab [:a :b :b] {}))))

