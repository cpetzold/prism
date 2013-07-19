(defproject prism "0.1.0-SNAPSHOT"
  :description "Prismatic frontend in cljs"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [noir "1.3.0"]
                 [prismatic/dommy "0.1.1"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :main prism.server
  :cljsbuild
  {:builds
   [{:source-path "src"
     :compiler
     {:output-to "resources/public/prism.js"
      :pretty-print true}}]})
