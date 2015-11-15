(ns markovify.core
  (require [clojure.core.async :refer [<!!]]
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
  (let [{:keys [text id] {:keys [user-mentions]} :entities {from :screen-name} :user} tweet
        seed (twitter/remove-mentions tweet)
        user-tweets (twitter/get-tweets (filter #(not= twitter/screen-name %) (map :screen-name user-mentions)))
        chain (utils/make-chain seed user-tweets)]
    (do (println (str "tweet from " from " saying " text))
        (twitter/post-reply
         id
         (build-tweet chain (str "@" from) (- 139 (count from)))))))

(defn -main
  [& args]
  (let [c (twitter/mentions-channel)]
    (println (str "connecting as " twitter/screen-name))
    (while true (receive-message (<!! c)))))
