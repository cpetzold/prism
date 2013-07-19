(ns prism.server
  (:require
   [noir.core :refer [defpage defpartial]]
   [noir.server :as server]
   [noir.response :as response]
   [hiccup.page :as page]
   [hiccup.element :as element]
   [prism.css :as css]))

(defpartial layout [& content]
  (page/html5
   [:head
    [:title "Prism"]
    (page/include-css
     "http://fonts.googleapis.com/css?family=Libre+Baskerville:400,700,400italic"
     "style.css")]
   [:body content]))

(defpage "/" []
  (layout
   (element/javascript-tag "var CLOSURE_NO_DEPS = true")
   (page/include-js "prism.js")))

(defpage "/style.css" []
  (response/content-type "text/css" (css/css)))

(def ports
  {:dev 4000
   :prod 4000})

(defn -main [& m]
  (let [mode (or (keyword (first m)) :dev)
        port (ports mode)]
    (server/start port {:mode (keyword mode) :ns 'prism})))