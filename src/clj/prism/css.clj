(ns prism.css
  (:require
   [garden.core :as garden]
   [garden.units :as u :refer [px percent]]))

(def colors
  {:bg :#fff
   :text :#444})

(def rules
  [:body
   {:background (:bg colors)
    :color (:text colors)
    :font {:family "Libre Baskerville"}}

   :h1
   {:font {:size (px 20)}}
   ])

(defn css []
  (garden/css
   {:output-style :expanded}
   rules))
