(ns starter.fxhash
  (:require [quil.core :as q]))

(defn fx-hash []
  (.-fxhash js/window))

(defn fx-rand []
  (.fxrand js/window))

(defn register-features [features]
  (set! js/window.$fxhashFeatures (clj->js features)))