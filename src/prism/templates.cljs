(ns prism.templates
  (:use [prism.utils :only [log truncate]])
  (:require-macros [hiccups.core :as hiccups])
  (:require [hiccups.runtime :as hiccupsrt]))

(def min-image-width 500)
(def max-image-height 500)

(hiccups/defhtml stories [ss]
  (for [s ss] (story s)))

(hiccups/defhtml story
  [s]
  (let 
    [url (.-url s)
     title (.-title s)
     feed (.-feed s)
     topics (map first (.-topics s))
     img (.-img s)
     img-visible (and img (>= (-> img .-size .-width) min-image-width))
     img-fill (if img (>= (-> img .-size .-height) max-image-height))]
    [:li
      {:class (str "story " (if img-fill "image-fill"))}
      [:div.story-info
        [:a.story-source {:href (.-url feed)} (.-title feed)]
        [:a.story-date {:href url :target "_blank"}
          (.fromNow (js/moment (.-date s)))]
        (for [topic topics]
          [:a.story-topic {:href "#"} topic])]
      [:div.story-content
        [:a.story-title {:href url :target "_blank"}
          [:h2 title]]
        (if img-visible
          [:a.story-image {:href url :target "_blank"}
            [:img
              {:src (.-url img)}]])
        [:p.story-text 
          (if img-visible
            (truncate (.-text s) 200 "…")
            (truncate (.-text s) 800 "…"))]
        [:div.clear]]]))