;; code from http://www.quil.info/

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

(defn terrain-height [t k]
  (q/ceil (* t (+ 9 k))))

(defn grid [w h]
  (map (fn [x] (map (fn [y] [x y]) (range 0 h))) (range 0 w)))

(def width 24)
(def height 24)

(defn setup []
  ; TODO: fix
  (f/register-features {:dark true :another-feature "yes"})
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  (q/noise-seed (* 100 (f/fx-rand)))
  ;; (q/no-smooth)
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.


  (let [gamma (f/fx-rand)
        delta (f/fx-rand)
        omega (f/fx-rand)
        features {:bars (and (< gamma 0.3) (> delta 0.7))
                  :points (and (< gamma 0.2) (< omega 0.3))
                  :autumn (< (q/abs (- gamma delta)) 0.15)
                  :blooooood (and (> gamma 0.7) (> delta 0.7))
                  :pale (and (> delta 0.4) (> omega 0.7))
                  :rainbow (and (> omega 0.1) (< omega 0.8) (< (* delta gamma) 0.3))
                  :cards (and (> delta 0.9)  (< omega 0.2))
                  :mono (< (* gamma delta) 0.02)}]

    ;; (js/console.log #js {:gamma gamma :delta delta :omega omega})
    ;; (js/console.log (clj->js features))
    (f/register-features features)

    {:gamma gamma
     :delta delta
     :omega omega
     :color 0
     :angle 0
     :t (* 100 (f/fx-rand))
     :grid (grid width height)}))

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (merge state
         {:color (mod (+ (:color state) 0.7) 255)
          :angle (+ (:angle state) 0.1)
          :t (inc (:t state))
          :grid (:grid state)}))

(def size 32)
(def space (+ size 3))

(defn draw-state [{:keys [grid t gamma delta omega]}]
  (let [t (/ t (* (+ 10 gamma delta)))]
    (q/fill 255 255 255)
  ;; (q/stroke (/ color 2) 255 255)
  ;; (q/stroke-weight 4)
    (q/background 0)
    (q/camera (+ 512 (* 100 (q/sin (/ t 5)))) (+ 512 (* 100 (q/sin (/ t 5)))) 512 0 0 0 0 0 -1)
    ;; (q/camera 250 250 250 0 0 0 0 0 -1)
    (q/ortho -640 640 -640 640 -1000 10000)
  ;; (q/specular 250)
    (q/directional-light 255 0 0 25 25 25)
    ;; (q/point-light 0 0 255 0 0 100)
    (q/ambient-light 50 100 50)
    ;; (q/sphere-detail 15)

    (q/with-translation [(- (/ (* width space) 2)) (- (/ (* height space) 2)) -128]
      (doseq [row grid]
        (doseq [[x y] row]
          (let [n (q/cos (* 2 q/PI (q/sin (* 0.5 q/PI (q/noise (* x (+ 0.15 (* 0.1 delta))) (* y (+ 0.15 (* 0.1 gamma))))))))]
            (doseq [z (range 0 (terrain-height n (* omega delta)))]
              (q/with-translation
                [(+ (* x space))
                 (+ (* y space))
                 (+ (+ (* space z)) (* 2 (q/sin (+ x y t))))]
                (q/fill 0 0 0)
                (q/stroke (+ 10 (* 255 gamma delta)  (* 50 (* 100 gamma delta) (/ z (+ 3.5 (* omega delta 3))) n))
                          (+ 100 (* 155 gamma delta (q/sin (* n omega 2 q/PI))))
                          (+ 10 (* omega delta 80) (* z (+ 32 (* 12 gamma)))))
                (q/stroke-weight 3)
                (q/rotate (* 2 q/PI omega (/ t 10) n))
                (q/box (- (* size gamma) (+ gamma delta omega) (* z gamma 1))
                       (* size omega (q/sin (* (/ z 5) t delta n)))
                       (* size gamma (q/cos  (* (/ z (+ 4 (* 2 gamma))) t omega n))))))))))))

; this function is called in index.html
(defn ^:export run-sketch []
  (q/defsketch noire
    :host "app"
    :size dims
    :renderer :p3d
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    ;; :key-pressed (u/save-image "iso.png")
    ; This sketch uses functional-mode middleware.
    ; Check quil wiki for more info about middlewares and particularly
    ; fun-mode.
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
