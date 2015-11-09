(ns markovify.twitter
  (require [clojure.string :as s]
           [markovify.utils :as utils]))

(def tweets
  {"@jarofghosts" ["not a real tweet" "maybe this is?" "this is some text" "why not?" "hey everyone why is my text so real?" "this is a statement i made once" "this is definitely a real tweet"
                   "continues to be real" "everything here is the truth" "oh no what am i doing" "i keep just generating text" "testing everything is good" "a thing is what this is" "huh?"
                   "real talk, this is a thing"]
   "@another-twitter-user" ["am i a real twitter user?" "oh no this is fake" "i keep all of my receipts" "testing is for suckers." "thing is my favorite addams family character" "text time"
                            "the truth is out there"]})

(defn get-tweets
  [users]
  (->> users
       (map #(get tweets %))
       flatten
       (filter (complement utils/is-twitter-username))
       (map #(s/split % #"\s+"))
       flatten))

(defn post-message
  [text]
  (println text))
