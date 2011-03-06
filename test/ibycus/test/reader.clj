(ns ibycus.test.reader
  (:use [ibycus.reader] :reload-all)
  (:use [clojure.test])
  (:require [techne.bag :as bag]))

(deftest test-string-to-words
  (is (= ["a" "bc"] (string->words "a bc")))
  (is (= ["a" "bc"] (string->words "a 123  bc")))
  (is (= ["a" "bc"] (string->words "a (bc)")))
         
         )
