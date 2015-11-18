(ns markovify.utils
  (require [clojure.string :as s]
           [markov.core :as markov]))

(defn invert-indices
  [indices]
  (->> indices
       flatten
       (into [0])
       (partition-all 2)))

(defn remove-indices
  [string bad-indices]
  (let [good-indices (invert-indices bad-indices)]
    (->> good-indices
         (map #(apply subs (into [string] %)))
         (apply str)
         s/trim)))

(defn join-str
  [prev item]
  (str prev (if (empty? prev) "" " ") item))

(defn make-chain
  [seed coll]
  (let [chain (markov/build-from-coll coll)]
    (if (empty? seed) (markov/generate-walk chain)
        (markov/generate-walk seed chain))))
