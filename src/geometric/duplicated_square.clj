(ns geometric.boxed-squares
  (:require [quil.middleware :as m]
            [quil.core :as q]
            [util.color :as c]))

(def x (atom 500))
(def y (atom 500))
(def size (atom 500))
(def max-size 800)
(def angle (atom 0))
(def mode (atom 0))
(def tick (atom 0))
(def tick-mode 2000)

(defn setup []
  (q/stroke 255 255 255)
  (q/background 0 0 0)
  (q/no-fill)
  (q/frame-rate 60))

(defn draw-square [x y s]
  (q/rect x y s s))

(defn draw []
  (let [new-x (mod (+ (q/random 1) @x 0) (q/width))
        new-y (mod (+ (q/random 1) @y 0) (q/height))
        new-angle (+ @angle (/ q/PI 200))
        new-size (mod (+ (q/random 2) @size 0) max-size)]
    (apply q/stroke (c/random-swiss-color))
    (q/with-translation [new-x new-y]
      (q/with-rotation [new-angle]
        (draw-square 0 0 new-size)))
    (reset! x new-x)
    (reset! y new-y)
    (reset! size new-size)
    (reset! angle new-angle)))

(defn draw-rotated-color-square []
  (let [new-x (+ (q/random 0) @x 0)
        new-y (+ (q/random 0) @y 0)
        new-angle (+ @angle (/ q/PI 200))]
    (apply q/stroke (c/random-swiss-color))
    (q/with-translation [new-x new-y]
      (q/with-rotation [new-angle]
        (draw-square 0 0 @size)))
    (reset! x new-x)
    (reset! y new-y)
    (reset! angle new-angle)))

(defn draw-small-random []
  (let [new-x (+ (q/random 1) @x 0)
        new-y (+ (q/random 1) @y 0)
        new-angle (+ @angle (/ q/PI 200))]
    (q/with-translation [new-x new-y]
      (q/with-rotation [new-angle]
        (draw-square 0 0 @size)))
    (reset! x new-x)
    (reset! y new-y)
    (reset! angle new-angle)))

(defn draw-rotated-square []
  (let [new-x (+ (q/random 0) @x 0)
        new-y (+ (q/random 0) @y 0)
        new-angle (+ @angle (/ q/PI 200))]
    (q/with-translation [new-x new-y]
      (q/with-rotation [new-angle]
       (draw-square 0 0 @size)))
    (reset! x new-x)
    (reset! y new-y)
    (reset! angle new-angle)))

(q/defsketch Duplicated-Square
  :size [3800 1400]
  :title "Duplicated-Square"
  :setup setup
  :draw draw)

(defn -main [& args])
