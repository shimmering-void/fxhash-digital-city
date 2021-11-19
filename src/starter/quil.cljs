(ns starter.quil
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [starter.util :as u]
            [starter.fxhash :as f]))

(defn get-viewport
  "Returns a vector of [vw vh] for the current browser window, scaled by the scale parameter.
   If true is passed to force-square the smaller dimension will be used on both axes."
  [scale force-square]
  (if force-square
    (let [dim (min (* scale (.-innerWidth js/window)) (* scale (.-innerHeight js/window)))]
      [dim dim])
    [(* scale (.-innerWidth js/window)) (* scale (.-innerHeight js/window))]))

(def dims (get-viewport 1 true))

(defn x
  "Return t% of viewport width, allows for resolution independent drawing i.e. 10px in a 100px window = (x 10) = 25px in a 250px window"
  [t]
  (let [[w _] dims] (* t (/ w 100))))

(defn y
  "Return t% of viewport height, allows for resolution independent drawing i.e. 10px in a 100px window = (x 10) = 25px in a 250px window"
  [t]
  (let [[_ h] dims] (* t (/ h 100))))

(defn setup []
  ;; EXAMPLE CODE, DO NOT HARDCODE YOUR FEATURES
  (f/register-features {:dark true :another-feature "yes"})

  (q/frame-rate 30)

  ; setup function returns initial state
  {:t 0
   :green (f/fx-rand)})

(defn update-state [state]
  (merge state {:t (inc (:t state))}))

(defn draw-state [{:keys [t green]}]
  (let [t' (/ t 10)]
    ; clear screen
    (q/fill 255 255 255)
    (q/stroke-weight 0)
    (q/background 0)
    ; fxhash functions
    (q/text (str "fxhash=" (f/fx-hash)) (x 2) (y 2))
    (q/text (str "fxrand=" (f/fx-rand)) (x 2) (y 4))
    ; draw resolution independent circle
    (let [[w h] dims]
      (q/fill (* 255 (q/sin t')) (* 255 green) (* 255 (q/cos t')))
      (q/ellipse (/ w 2) (/ h 2) (x 10) (y 10)))))

; this function is called in index.html
(defn ^:export run-sketch []
  (q/defsketch fxhash
    :host "app"
    :size dims
    :renderer :p2d
    :setup setup
    :update update-state
    :draw draw-state
    ;; :key-pressed (u/save-image "export.png")
    :middleware [m/fun-mode]))

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (js/console.log "start"))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (run-sketch)
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
