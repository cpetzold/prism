(defproject prism "0.1.0-SNAPSHOT"
  :description "Prismatic frontend in cljs"
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [compojure "1.1.5"]
                 [lib-noir "0.6.6"]
                 [garden "0.1.0-beta6"]
                 [prismatic/dommy "0.1.1"]]
  :plugins [[lein-ring "0.8.6"]
            [lein-cljsbuild "0.3.2"]]
  :source-paths ["src/clj"]
  :ring {:handler prism.server/routes}
  :cljsbuild
  {:builds
   [{:source-paths ["src/cljs"]
     :compiler
     {:output-to "resources/public/prism.js"
      :pretty-print true}}]})
