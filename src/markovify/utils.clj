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
  (markov/generate-walk seed (markov/build-from-coll coll)))

(defn parse-message
  [message]
  (let [pieces (split-with is-twitter-username (s/split message #"\s+"))]
    {:users (first pieces) :seed (s/join " " (second pieces))}))
