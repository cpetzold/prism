(ns prism.core
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]
   [dommy.macros :refer [node]])
  (:require
   [cljs.core.async :refer [chan put! <! !>]]
   [dommy.core :as dommy]))

(defn request* [url cb]
  (let [fn-name (gensym "fn")
        url (format "%s%scallback=%s"
                    url
                    (if (neg? (.indexOf url "?")) "?" "&")
                    fn-name)
        script (node [:script {:src url}])]
    (aset js/window fn-name
          (fn [res]
            (dommy/remove! script)
            (cb res)))
    (dommy/append! js/document.head script)))

(defn request [url]
  (let [c (chan)]
    (request* url #(put! c %))
    c))

(defn listen [el type]
  (let [c (chan)]
    (dommy/listen! el type #(put! c %))
    c))

(let [click (listen js/window :click)]
  (go
   (while true
     (<! click)
     (js/console.log (<! (request "//api.getprismatic.com/news/home"))))))