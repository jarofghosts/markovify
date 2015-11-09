(ns markovify.core
  (require [clojure.string :as s]
           [markov.core :as markov]))

(defn- join-str
  [prev item]
  (str prev (if (empty? prev) "" " ") item))

(defn- is-twitter-username
  [string]
  (.startsWith string "@"))

(defn make-chain
  [seed coll]
  (markov/generate-walk seed (markov/build-from-coll coll)))

(defn parse-message
  [message]
  (let [pieces (split-with is-twitter-username (s/split message #"\s+"))]
    {:users (first pieces) :seed (s/join " " (second pieces))}))

(def tweets
  {"@jarofghosts" ["not a real tweet" "maybe this is?" "this is some text" "why not?" "hey everyone why is my text so real?" "this is a statement i made once" "this is definitely a real tweet"
                   "continues to be real" "everything here is the truth" "oh no what am i doing" "i keep just generating text" "testing everything is good" "a thing is what this is" "huh?"
                   "real talk, this is a thing"]
   "@another-twitter-user" ["am i a real twitter user?" "oh no this is fake" "i keep all of my receipts" "testing is for suckers." "thing is my favorite addams family character" "text time"
                            "the truth is out there"]})

(defn get-tweets
  [users]
  (flatten
   (map #(s/split % #"\s+")
        (filter (complement is-twitter-username)
                (flatten
                 (map #(get tweets %) users))))))

(defn- post-message
  [text]
  (println text))

(defn receive-message
  [from message]
  (let [{:keys [users seed]} (parse-message message)]
    (post-message (build-tweet (make-chain seed (get-tweets users)) from))))

(defn build-tweet
  ([words] (build-tweet words ""))
  ([words initial] (build-tweet words initial 140))
  ([words initial max-chars]
   (last
    (take-while
     #(< (count %) max-chars) (reductions join-str initial words)))))
