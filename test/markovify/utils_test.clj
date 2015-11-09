(ns markovify.utils-test
  (:require [clojure.test :refer :all]
            [markovify.utils :refer :all]))

(deftest twitter-username-test
  (testing "is-twitter-username correctly determines if a string is a twitter
            username"
    (is (true? (is-twitter-username "@username")))
    (is (true? (is-twitter-username "@anotherName")))
    (is (false? (is-twitter-username "username")))
    (is (false? (is-twitter-username "anotherName")))))

(deftest parse-message-test
  (testing "parse-message splits usernames and seeds"
    (is (= (parse-message "@username seed") {:seed "seed" :users ["@username"]}))
    (is (= (parse-message "@username @anotherName a longer seed")
           {:seed "a longer seed" :users ["@username" "@anotherName"]}))))
