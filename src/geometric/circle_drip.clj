(ns geometric.circle-drip
  (:require [quil.middleware :as m]
            [util.color :as c]
            [quil.core :as q]))

(def do-phase-x (atom false))
(def delta-size 10)
(def nb-circle 20)

(defn setup []
  (q/background 255 255 255)
  (q/stroke 1)
  (q/color 0 0 0)
  (q/frame-rate 1))

(defn draw-circle [x y s]
  (q/ellipse x y s s))

(defn draw []
  (doseq [dx (range 0 (q/width) (* nb-circle delta-size))
          dy (range 0 (q/height) (* nb-circle delta-size))]
    (swap! do-phase-x not)
    (q/with-translation [dx dy]
     (let [x (/ (* nb-circle delta-size) 2)
           y (/ (* nb-circle delta-size) 2)]
       (doseq [size (range (* nb-circle delta-size) 0 (* delta-size -1))]
         (let [i (/ size delta-size)
               color-stroke (c/random-nippon-color)
               color-fill (c/random-nippon-color)]
           (apply q/fill color-fill)
           (apply q/stroke color-stroke)
           (if @do-phase-x
             (do
               (draw-circle (- x (* i 5)) y size)
               (draw-circle (+ x (* i 5)) y size))
             (do
               (draw-circle x (- y (* i 5)) size)
               (draw-circle x (+ y (* i 5)) size)))))))))

(q/defsketch Boxed-Squares
  :size [3800 1400]
  :title "Circle-drip"
  :setup setup
  :draw draw)

(defn -main [& args])
