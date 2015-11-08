(ns markovify.core)

(defn- join-str
  [prev item]
  (str prev (if (empty? prev) "" " ") item))

(defn build-tweet
  ([words] (build-tweet words ""))
  ([words initial]
    (last (take-while #(< (count %) 140) (reductions join-str initial words)))))
