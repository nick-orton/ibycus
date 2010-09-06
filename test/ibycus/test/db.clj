(ns ibycus.test.db
  (:use [ibycus.db] :reload-all)
  (:use [clojure.test])
  )


(deftest test-build-fixture
  (create-vocab)
  (drop-vocab))
