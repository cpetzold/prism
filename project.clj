(defproject prism "0.1.0-SNAPSHOT"
  :description "Prismatic frontend in cljs"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir "1.3.0"]
                 [garden "0.1.0-beta6"]
                 [prismatic/dommy "0.1.1"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :source-paths ["src/clj"]
  :main prism.server
  :cljsbuild
  {:builds
   [{:source-path "src/cljs"
     :compiler
     {:output-to "resources/public/prism.js"
      :pretty-print true}}]})
