(ns prism.core
  (:use [clojure.string :only [replace-first]]
        [jayq.core :only [$ ajax append on add-class
                          remove-class prev next anim]]
        [prism.utils :only [log in? in-sub?]])
  (:require [goog.net.Jsonp :as gjsonp]
            [prism.templates :as templates]))

(def $window ($ js/window))
(def $stories ($ :#stories))

(def window-size (atom nil))

(def top-offset (atom 0))

(def selected-story (atom nil))
(def selected-class "selected")
(def focus-class "focus")

(def prevent-scroll-fire (atom false))

(def key-map
  {:space 32
   :left 37
   :up 38
   :right 39
   :down 40
   :j 74
   :k 75})

(def key-set (vals key-map))

; (defn scroll-to [n]
;   (anim $window {:scrollTop n} 100))

(defn scroll-to [n]
  (.scrollTop $window n))

(defn scroll-to-story
  [$story]
  (let [story-top (-> $story .offset .-top)
        story-height (.outerHeight $story)
        window-height (:height @window-size)
        scroll-top (- story-top (- (/ window-height 2) (/ story-height 2)))]
    (reset! prevent-scroll-fire true)
    (scroll-to (Math/round (- story-top @top-offset)))))

(defn select-story
  [$story]
  (when-not (empty? $story)
    (remove-class ($ (str "." selected-class)) selected-class)
    (add-class $stories focus-class)
    (add-class $story selected-class)
    (reset! selected-story $story)
    (scroll-to-story $story)))

(defn select-prev-story []
  (select-story
    (prev @selected-story)))

(defn select-next-story []
  (select-story
    (next @selected-story)))

(defn handle-keydown
  [e]
  (let [code (.-keyCode e)]
    (log code)
    (when (in? key-set code) (.preventDefault e))
    (condp (partial in-sub? key-map) code
      ; Up
      [:up :k] (select-prev-story)
      ; Down
      [:down :j :space] (select-next-story)
      nil)))

(defn set-window-size []
  (reset! window-size
    {:width (.innerWidth $window)
     :height (.innerHeight $window)}))

(defn set-top-offset []
  (let [window-height (:height @window-size)
        offset (if (< window-height 900) 0 100)]
    (reset! top-offset offset)
    (when @selected-story (scroll-to-story @selected-story))))

(defn handle-resize
  [e]
  (set-window-size))
  ; (set-top-offset))

(defn handle-scroll
  [e]
  (if @prevent-scroll-fire
    (reset! prevent-scroll-fire false)
    (remove-class $stories focus-class)))

(defn bind-events []
  (on $window :scroll handle-scroll)
  (on $window :keydown handle-keydown)
  (on $window :resize handle-resize))

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
  (set-window-size)
  ; (set-top-offset)
  (load-stories "news/home"
    (fn [stories]
      (select-story ($ "#stories .story:first-child"))))
  (bind-events))

(main)