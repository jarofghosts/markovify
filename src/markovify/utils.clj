(ns markovify.utils
  (require [clojure.string :as s]
           [markov.core :as markov]))

(defn join-str
  [prev item]
  (str prev (if (empty? prev) "" " ") item))

(defn is-twitter-username
  [string]
  (.startsWith string "@"))

(defn make-chain
  [seed coll]
  (let [chain (markov/build-from-coll coll)]
    (if (empty? seed) (markov/generate-walk chain)
        (markov/generate-walk seed chain))))

(defn parse-message
  [message]
  (let [pieces (split-with is-twitter-username (s/split message #"\s+"))]
    {:users (first pieces) :seed (s/join " " (second pieces))}))
