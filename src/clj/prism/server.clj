(ns prism.server
  (:require
   [noir.server :as server]
   [noir.core :refer [defpage defpartial]]
   [hiccup.page :as page]
   [hiccup.element :as element]))

(defpartial layout [& content]
  (page/html5
   [:head
    [:title "Prism"]
    (page/include-css "style.css")]
   [:body content]))

(defpage "/" []
  (layout
   (element/javascript-tag "var CLOSURE_NO_DEPS = true")
   (page/include-js "prism.js")))

(def ports
  {:local 4000
   :prod 4000})

(defn -main [& m]
  (let [mode (or (keyword (first m)) :local)
        port (ports mode)]
    (server/start port {:mode (keyword mode) :ns 'prism})))