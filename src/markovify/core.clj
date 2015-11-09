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
  (let [{:keys [users seed]} (utils/parse-message message)
        user-tweets (twitter/get-tweets users)
        chain (utils/make-chain seed user-tweets)]
    (twitter/post-message
     (build-tweet chain from (- 140 (count from))))))
