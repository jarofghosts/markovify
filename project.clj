(defproject markovify "0.1.0-SNAPSHOT"
  :description "markov-generating Twitter bot"
  :url "https://twitter.com/markovify"
  :main markovify.core
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [cheshire "5.5.0"]
                 [janiczek/markov "0.3.0"]
                 [environ "1.0.1"]
                 [twitter-api "0.7.8"]])
