(ns prism.core
  (:use [clojure.string :only [replace-first]]
        [jayq.core :only [$ ajax append on add-class
                          remove-class prev next anim]]
        [prism.utils :only [log in?]])
  (:require [goog.net.Jsonp :as gjsonp]
            [prism.templates :as templates]))

(def $window ($ js/window))
(def $stories ($ :#stories))

(def window-size (atom
  {:width (.innerWidth $window)
   :height (.innerHeight $window)}))

(def selected-story (atom nil))
(def selected-class "selected")
(def focus-class "focus")

(def prevent-scroll-fire (atom false))

(def key-map
  {:left 37
   :up 38
   :right 39
   :down 40})

(def key-set (vals key-map))

; (defn scroll-to [n]
;   (anim $window {:scrollTop n} 100))

(defn scroll-to [n]
  (.scrollTop $window n))

(defn select-story
  [$story]
  (if-not (empty? $story)
    (let [story-top (-> $story .offset .-top)
          story-height (.outerHeight $story)
          window-height (:height @window-size)
          scroll-top (- story-top (- (/ window-height 2) (/ story-height 2)))]
      (remove-class ($ (str "." selected-class)) selected-class)
      (add-class $stories focus-class)
      (add-class $story selected-class)
      (reset! prevent-scroll-fire true)
      (scroll-to (- story-top (/ window-height 6)))
      (reset! selected-story $story))))

(defn select-prev-story []
  (select-story
    (prev @selected-story)))

(defn select-next-story []
  (select-story
    (next @selected-story)))

(defn handle-keydown
  [e]
  (let [code (.-keyCode e)]
    (if (in? key-set code)
      (.preventDefault e))
    (condp = code
      (:up key-map) (select-prev-story)
      (:down key-map) (select-next-story)
      nil)))

(defn handle-scroll
  [e]
  (if @prevent-scroll-fire
    (reset! prevent-scroll-fire false)
    (remove-class $stories focus-class)))

(defn bind-events []
  (on $window :scroll handle-scroll)
  (on $window :keydown handle-keydown))

(defn fetch-stories
  [path callback]
  (let [path (replace-first path #"^/" "")
        url (str "http://api.getprismatic.com/" path)]
    (ajax url
      {:dataType "jsonp"
       :data {:limit 25}
       :success callback})))

(defn load-stories
  [path callback]
  (fetch-stories path
    (fn [data]
      (let [stories (.-docs data)]
        (append $stories (templates/stories stories))
        (callback stories)))))

(defn main []
  (load-stories "news/home"
    (fn [stories]
      (select-story ($ "#stories .story:first-child"))))
  (bind-events))

(main)