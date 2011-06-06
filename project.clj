(defproject ibycus "0.1.0-SNAPSHOT"
  :description "writes epic verse via markov chain"
  :dev-dependencies [[lein-clojars "0.6.0"]
                     [marginalia "0.5.1"]]
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [techne "0.7.0"]]
  :keep-non-project-classes true
  :main ibycus.main)
