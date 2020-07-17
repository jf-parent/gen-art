(ns geometric.boxed-squares
  (:require [quil.middleware :as m]
            [quil.core :as q]
            [util.color :as c]))

(def get-color (nth [c/random-color c/random-swiss-color c/random-nippon-color] 1))

(defn get-shape []
  (rand-nth [:triangle :rectangle :ellipse]))

(defn setup-draw-boxed-squares []
  (q/background 255 255 255)
  (q/no-stroke)
  (q/frame-rate 60))

(defn draw-triangle [x1 y1 x2 y2 x3 y3]
  (q/triangle x1 y1 x2 y2 x3 y3))

(defn draw-square [x y s]
  (q/rect x y s s))

(defn draw-ellipse [x y s]
  (q/ellipse x y s s))

(defn draw-boxed-squares []
  (doseq [dx (range 0 (q/width) 100)
          dy (range 0 (q/height) 100)]
    (q/with-translation [dx dy]
      (let [shape (get-shape)
            end-range (if (= shape :triangle) 60 100)
            step-range (if (= shape :triangle) 10 10)
            fill-on-off (take 10 (cycle [false true]))]
        ;; Reset
        (apply q/fill (get-color))
        (draw-square 0 0 100)

        (doseq [d (range 0 end-range step-range)]
          ;; Stroke
          (apply q/stroke (get-color))

          ;; Fill
          (if fill-on-off
            (apply q/fill (get-color))
            (q/no-fill))

          ;; Shape
          (case shape
            :triangle (draw-triangle 50 (/ d 2) (/ d 2) (- 100 (/ d 2)) (- 100 (/ d 2)) (- 100 (/ d 2)))
            :ellipse (draw-ellipse 50 50 (- 100 d))
            :rectangle (draw-square (/ d 2) (/ d 2) (- 100 d))))))))

(q/defsketch Boxed-Squares
  :size [3800 1400]
  :title "Boxed-Squares"
  :setup setup-draw-boxed-squares
  :draw draw-boxed-squares)

(defn -main [& args])
