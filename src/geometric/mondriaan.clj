(ns geometric.mondriaan
  (:require [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]
            [quil.core :as q]))

(def tick (atom 0))
(def stroke-weight 10)
(def color-init [:white :white :white :black :yellow :blue :red])
(def color-bank (atom (shuffle color-init)))

(defn get-division-min-max [width height]
  (let [min_ (* stroke-weight 6)]
    [min_ (math/floor (- (min width height) (* min_ 1.5)))]))

(def colors {:yellow [255 240 1]
             :red [255 1 1]
             :blue [1 1 253]
             :white [249 249 249]
             :black [0 0 0]})

(defn get-color-key []
  (when (zero? (count @color-bank))
    (reset! color-bank (shuffle color-init)))
  (let [next-color (first @color-bank)]
    (reset! color-bank (rest @color-bank))
    next-color))

(defn setup []
  (q/background 255 255 255)
  (q/stroke-weight 10)
  (q/stroke 0 0 0)
  (q/frame-rate 5))

(defn draw-in-subregion-horizontal [depth x1 y1 x2 y2 force]
  (let [region-width (- x2 x1)
        region-height (- y2 y1)
        [min-subregion max-subregion] (get-division-min-max region-width region-height)
        width (- x2 x1)
        height1 (+ min-subregion (rand-int max-subregion))
        height2 (- region-height height1)]
    (apply q/fill (colors (get-color-key)))
    (q/rect x1 y1 width height1)
    (draw-in-subregion-dispatch (inc depth) x1 y1 x2 (+ y1 height1) :vertical)
    (apply q/fill (colors (get-color-key)))
    (q/rect x1 (+ y1 height1) width height2)
    (draw-in-subregion-dispatch (inc depth) x1 (+ y1 height1) x2 y2 :vertical)))

(defn draw-in-subregion-vertical [depth x1 y1 x2 y2 force]
  (let [region-width (- x2 x1)
        region-height (- y2 y1)
        [min-subregion max-subregion] (get-division-min-max region-width region-height)
        height (- y2 y1)
        width1 (+ min-subregion (rand-int max-subregion))
        width2 (- region-width width1)]
    (apply q/fill (colors (get-color-key)))
    (q/rect x1 y1 width1 height)
    (draw-in-subregion-dispatch (inc depth) x1 y1 (+ x1 width1) y2 :horizontal)
    (apply q/fill (colors (get-color-key)))
    (draw-in-subregion-dispatch (inc depth) (+ x1 width1) y1 x2 y2 :horizontal)))

(defn draw-in-subregion-dispatch [depth x1 y1 x2 y2 force]
  (when (< depth 5)
    (let [probability-subdivize (math/floor (max 80 (/ 100 depth)))]
      (when (<= (rand-int 100) probability-subdivize)
        (draw-in-subregion depth x1 y1 x2 y2 force)))))

(defn draw-in-subregion [depth x1 y1 x2 y2 force]
  (if force
    (if (= force :vertical)
      (draw-in-subregion-vertical depth x1 y1 x2 y2 false)
      (draw-in-subregion-horizontal depth x1 y1 x2 y2 false))
    (let [chance (rand-int 100)]
      (if (>= chance 50)
        (draw-in-subregion-horizontal depth x1 y1 x2 y2 false)
        (draw-in-subregion-vertical depth x1 y1 x2 y2 false)))))

(defn tableau-i []
  (swap! tick inc)
  (when (zero? (mod @tick 4))
    (q/background 255 255 255)
    (draw-in-subregion-dispatch 1 0 0 (q/width) (q/height) false)))

(defn draw []
  (tableau-i))

(q/defsketch Mondriaan
  :size [2000 1250]
  :title "Mondriaan Generator"
  :setup setup
  :draw draw)

(defn -main [& args])
