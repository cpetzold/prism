(ns prism.server
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :refer [resources]]
   [noir.response :as response]
   [hiccup.page :as page]
   [hiccup.element :as element]
   [prism.css :as css]))

(defn layout [& content]
  (page/html5
   [:head
    [:title "Prism"]
    (page/include-css
     "http://fonts.googleapis.com/css?family=Oswald:400,300,700"
     "http://fonts.googleapis.com/css?family=Libre+Baskerville:400,700,400italic"
     "style.css")]
   [:body content]))

(defroutes routes
  (GET "/style.css" [] (response/content-type "text/css" (css/css)))
  (GET "/" []
       (layout
        (element/javascript-tag "var CLOSURE_NO_DEPS = true")
        (page/include-js "prism.js")))
  (resources "/"))
