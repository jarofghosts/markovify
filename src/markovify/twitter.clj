(ns markovify.twitter
  (require [clojure.string :as s]
           [clojure.data.json :as json]
           [clojure.core.async :refer [>! chan go]]
           [markovify.utils :as utils]
           [environ.core :refer [env]]
           [twitter.api.restful :as twitter-api]
           [twitter.api.streaming :as twitter-stream]
           [twitter.oauth :as oauth])
  (use [twitter.callbacks]
       [twitter.callbacks.handlers])
  (import (twitter.callbacks.protocols SyncSingleCallback AsyncStreamingCallback)))

(def screen-name (env :markovify-screen-name))
(def app-consumer-key (env :app-consumer-key))
(def app-consumer-secret (env :app-consumer-secret))
(def user-access-token (env :user-access-token))
(def user-access-token-secret (env :user-access-token-secret))

(def creds (oauth/make-oauth-creds app-consumer-key
                                   app-consumer-secret
                                   user-access-token
                                   user-access-token-secret))

(def seen-tweets (atom []))

(defn remove-mentions
  [tweet]
  (let [{:keys [text] {:keys [user-mentions]} :entities} tweet]
    (utils/remove-indices text (map :indices user-mentions))))

(defn mentions?
  [tweet sn]
  (let [user-mentions (get-in tweet [:entities :user-mentions])]
    (->> user-mentions
         (map :screen-name)
         (filter #(= sn %))
         empty?
         not)))

(defn get-user-tweets
  [user-handle]
  (let [user (s/replace-first user-handle #"@" "")]
    (twitter-api/statuses-user-timeline
     :oauth-creds creds :params {:screen-name user
                                 :exclude-replies true
                                 :contributor-details false
                                 :trim-user true
                                 :include-rts false}
     :callbacks (SyncSingleCallback. response-return-body
                                     response-throw-error
                                     exception-rethrow))))

(defn mentions-to-channel
  [chan]
  (fn [_ baos]
    (go
      (let [json-text (str baos)
            tweet (json/read-str json-text :key-fn keyword)]
        (when (mentions? tweet screen-name)
          (>! chan tweet))))))

(defn mentions-channel
  []
  (let [c (chan)]
    (twitter-stream/user-stream :oauth-creds creds :callbacks (AsyncStreamingCallback.
                                                               (mentions-to-channel c)
                                                               response-throw-error
                                                               exception-rethrow))
    c))

(defn get-tweets
  [users]
  (->> users
       (map #(get-user-tweets %))
       flatten
       (map remove-mentions)
       (map #(s/split % #"\s+"))
       flatten))

(defn post-message
  [text]
  (println text))
