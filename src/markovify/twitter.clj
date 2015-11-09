(ns markovify.twitter
  (require [clojure.string :as s]
           [markovify.utils :as utils]
           [environ.core :refer [env]]
           [twitter.api.restful :as twitter-api]
           [twitter.oauth :as oauth])
  (use [twitter.callbacks]
       [twitter.callbacks.handlers])
  (import (twitter.callbacks.protocols SyncSingleCallback)))

(def app-consumer-key (env :app-consumer-key))
(def app-consumer-secret (env :app-consumer-secret))
(def user-access-token (env :user-access-token))
(def user-access-token-secret (env :user-access-token-secret))

(def creds (oauth/make-oauth-creds app-consumer-key
                                   app-consumer-secret
                                   user-access-token
                                   user-access-token-secret))

(defn get-user-tweets
  [user-handle]
  (let [user (s/replace-first user-handle #"@" "")]
    (into []
          (map :text
               (twitter-api/statuses-user-timeline
                :oauth-creds creds :params {:screen-name user
                                            :exclude-replies true
                                            :contributor-details false
                                            :trim-user true
                                            :include-rts false}
                :callbacks (SyncSingleCallback. response-return-body
                                                response-throw-error
                                                exception-rethrow))))))

(defn get-tweets
  [users]
  (->> users
       (map #(get-user-tweets %))
       flatten
       (map #(s/split % #"\s+"))
       flatten
       (filter (complement utils/is-twitter-username))))

(defn post-message
  [text]
  (println text))
