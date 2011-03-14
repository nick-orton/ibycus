(ns ibycus.test.tokenizer
  (:use [ibycus.tokenizer] :reload-all)
  (:use [clojure.test])
  (:require [techne.bag :as bag]))

(defn- assert-tokenization [tokens string]
  (is (= tokens (string->words string))))

(deftest test-string-to-words
  (assert-tokenization ["a" "bc"] "a bc")
  (assert-tokenization ["a" "bc"] "a 123  bc")
  (assert-tokenization ["a" "bc"] "a (bc)")
  (assert-tokenization ["a" "bc"] "a.. bc")
  (assert-tokenization ["a" "." "bc"] "a. bc")
  (assert-tokenization ["a" "bc"] "--a-- bc")
  (assert-tokenization ["a" "bc"] "a ll bc")
  (assert-tokenization ["cost" "cutting"] "cost:cutting")
  (assert-tokenization ["cost" "cutting"] "cost#cutting")
  (assert-tokenization ["cost" "cutting"] "cost[cutting]")
  (assert-tokenization ["cost" "cutting"] "cost(cutting)"))


