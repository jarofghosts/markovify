(ns markovify.core
  (require [clojure.core.async :refer [<! go]]
           [markovify.utils :as utils]
           [markovify.twitter :as twitter]
           [environ.core :refer [env]]))

(defn- build-tweet
  ([words] (build-tweet words ""))
  ([words initial] (build-tweet words initial 140))
  ([words initial max-chars]
   (last
    (take-while
     #(< (count %) max-chars) (reductions utils/join-str initial words)))))

(defn receive-message
  [tweet]
  (let [{:keys [text] {:keys [user-mentions]} :entities {from :screen-name} :user} tweet
        seed (twitter/remove-mentions tweet)
        user-tweets (twitter/get-tweets (map :screen-name user-mentions))
        chain (utils/make-chain seed user-tweets)]
    (twitter/post-message
     (build-tweet chain (str "@" from) (- 139 (count from))))))

(defn main
  []
  (let [c (twitter/mentions-channel)]
    (go (while true (receive-message <! c)))))
