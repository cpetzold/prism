(defproject prism "0.1.0-SNAPSHOT"
  :description "Prismatic frontend in cljs"
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [noir "1.3.0"]
                 [garden "0.1.0-beta6"]
                 [prismatic/dommy "0.1.1"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :source-paths ["src/clj"]
  :main prism.server
  :cljsbuild
  {:builds
   [{:source-paths ["src/cljs"]
     :compiler
     {:output-to "resources/public/prism.js"
      :pretty-print true}}]})
