(ns markovify.utils-test
  (:require [clojure.test :refer :all]
            [markovify.utils :refer :all]))

(deftest invert-indices-test
  (testing "converts vector of bad indices into vector of good indices"
    (is [[0 0] [12 15] [27]] (invert-indices [[0 12] [15 27]]))
    (is [[0]] (invert-indices [[]]))))

(deftest remove-indices-test
  (testing "removes substrings at specified indices"
    (is "good" (remove-indices "goodbad" [[4 6]]))
    (is
     "this is a sentence"
     (remove-indices "this is a bad sentence with problems" [[10 12] [23 35]]))))
