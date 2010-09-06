(ns ibycus.test.reader
  (:use [ibycus.reader] :reload-all)
  (:use [clojure.test]))


(deftest test-read-seq
  (is (= {:a {:a 1}} (words->vocab [:a :a] {})))
  (is (= {:a {:a 2}} (words->vocab [:a :a :a] {})))
  (is (= {:a {:a 1 :b 1}} (words->vocab [:a :a :b] {})))
  (is (= {:a {:b 1} :b {:b 1}} (words->vocab [:a :b :b] {}))))
