(ns ibycus.test.reader
  (:use [ibycus.reader] :reload-all)
  (:use [clojure.test])
  )


(deftest test-read-seq
  (is (= {:a {:a 1}} (read-seq [:a :a] {})))
  (is (= {:a {:a 2}} (read-seq [:a :a :a] {})))
         
         )
