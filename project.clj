(defproject prism "0.1.0-SNAPSHOT"
            :description "Prismatic frontend in cljs"
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [org.clojure/tools.nrepl "0.2.0-beta10"]
                           [hiccups "0.1.1"]
                           [jayq "0.2.0"]]
            :plugins [[lein-cljsbuild "0.2.9"]]
            :cljsbuild {
              :builds [{
                :build nil
                :source-path "src"
                :compiler {
                  :output-to "js/main.js"
                  :optimizations :simple
                  :pretty-print true
                }}]})
