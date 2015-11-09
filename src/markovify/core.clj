(ns markovify.core
  (require [markovify.utils :as utils]
           [markovify.twitter :as twitter]))

(defn- build-tweet
  ([words] (build-tweet words ""))
  ([words initial] (build-tweet words initial 140))
  ([words initial max-chars]
   (last
    (take-while
     #(< (count %) max-chars) (reductions utils/join-str initial words)))))

(defn receive-message
  [from message]
  (let [{:keys [users seed]} (utils/parse-message message)]
    (twitter/post-message
     (build-tweet (utils/make-chain seed (twitter/get-tweets users)) from))))
