(ns prism.utils)

(defn in?
  [seq elm]  
  (some #(= elm %) seq))

(defn in-sub?
  [map keys elm]
  (in?
    (vals (select-keys map keys))
    elm))

(defn log [& more]
  (.apply (.-log js/console) js/console
    (into-array (map #(if (satisfies? cljs.core.ISeqable %)
      (pr-str %)
      %)
    more))))

(defn truncate
  [string length ellipsis]
  (if (<= (.-length string) length)
    string
    (let [string (.substr string 0 length)]
      (str string " " ellipsis))))
