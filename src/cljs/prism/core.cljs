(ns prism.core
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]
   [dommy.macros :refer [node deftemplate]])
  (:require
   [clojure.string :refer [join]]
   [cljs.core.async :refer [chan put! take! close! <! >!]]
   [dommy.core :as dommy]))

(def +api-host+ "//api.getprismatic.com")

(defn map->query-string
  "{:foo \"bar\" :lorem \"ipsum\"} => foo=bar&lorem=ipsum"
  [query-params]
  (->> query-params
       (sort-by first)
       (map  (fn [[k v]]
               (str (name k) "=" (js/encodeURIComponent v))))
       (join "&")))

(defn request* [url cb]
  (let [fn-name (gensym "fn")
        url (format "%s%scallback=%s&to-remove=none"
                    url
                    (if (neg? (.indexOf url "?")) "?" "&")
                    fn-name)
        script (node [:script {:src url}])]
    (aset js/window fn-name
          (fn [res]
            (dommy/remove! script)
            (cb (js->clj res :keywordize-keys true))))
    (dommy/append! js/document.head script)))

(defn request [url]
  (let [c (chan)]
    (request* url #(put! c %))
    c))

(defn listen [el type]
  (let [c (chan)]
    (dommy/listen! el type #(put! c %))
    c))

(defn feed [interest next]
  (request (format "%s/%s/%s?api-version=1.22&%s"
                   +api-host+
                   (:type interest)
                   (:key interest)
                   (-> next :query-params map->query-string))))

(defn paginate [interest channel-size]
  (let [c (chan channel-size)]
    (go
     (loop [next {}]
       (let [res (<! (feed interest next))]
         (>! c res)
         (if (or (nil? (:next res))
                 (< (-> res :next :query-params :first-article-idx)
                    (-> next :query-params :first-article-idx)))
           (close! c)
           (recur (:next res))))))
    c))

(deftemplate full-screen-article
  [{:keys [title text images]}]
  [:.article-container
   [:.article
    (when (seq images)
      [:.article-image
       {:style {:background-image (format "url('%s')" (-> images first :url))}}])
    [:h1 title]
    [:p text]]])

(let [click (listen js/window :click)
      next-page (paginate {:type "news" :key "home"} 2)]
  (go
   (loop [article-queue []]
     (let [[article & queue] article-queue]
       (js/console.log (clj->js article))
       (if article
         (do
           (->> article
                full-screen-article
                (dommy/append! js/document.body))
           (recur queue))
         (let [articles (:docs (<! next-page))
               queue (concat queue articles)]
           (when (seq queue) (recur queue))))))))