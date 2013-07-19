(ns prism.core
  (:require
   [dommy.core :as dommy]))

(dommy/append! js/document.body [:h1 "Hello, world!"])