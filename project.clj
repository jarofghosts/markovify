(defproject markovify "0.1.0-SNAPSHOT"
  :description "markov-generating Twitter bot"
  :url "https://twitter.com/markovify"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [janiczek/markov "0.3.0"]
                 [environ "1.0.1"]
                 [twitter-api "0.7.8"]])
