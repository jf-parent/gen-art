(ns geometric.triangular-tiling
  (:require [quil.middleware :as m] [quil.core :as q]))

(set! *warn-on-reflection* true)

(def row (atom 0))
(def column (atom 0))

(defn random-color [nb]
  (take nb (repeatedly (fn [] [(q/random 255) (q/random 255) (q/random 255)]))))

(defn setup-draw-triangular-tiling-p3m1-live []
  (q/background 255 255 255)
  (q/no-stroke)
  (q/frame-rate 60))

(defn setup-random-from-triangular-tiling-live []
  (q/background (q/random 255) (q/random 255) (q/random 255))
  (q/no-stroke)
  (q/frame-rate 60))

(defn setup-random-from-triangular-tiling-not-live []
  (q/background (q/random 255) (q/random 255) (q/random 255))
  (q/no-stroke)
  (q/no-loop))

(defn setup-triangular-tiling-p3m1-not-live []
  (q/background (q/random 255) (q/random 255) (q/random 255))
  (q/no-stroke)
  (q/no-loop))

(defn draw-random-from-triangular-tiling-live []
  (let [max-height 5
        max-width 5
        colors (random-color 20)]
    (dotimes [i 2]
      (let [width (q/random 2 max-width)
            height (q/random 2 max-height)
            x0 (* (/ width 2) @column)
            y0 (* (/ height 2) @row)
            x1 (+ (/ width 2) x0)
            y1 (+ (/ width 2) y0)
            x2 (+ x0 width)
            y2 y0]
        (apply q/fill (nth colors (q/random (min (count colors) (+ 0 (quot (* (max @row 40) (max @column 80)) (q/random 500 1500)))))))
        (q/with-rotation [(q/random (* -1 (/ q/PI 2)) (/ q/PI 2))]
          (q/triangle x0 y0 x1 y1 x2 y2))))
    (if (< @column (/ (q/width) max-width))
      (swap! column inc)
      (do
        (swap! row inc)
        (reset! column 0)))))

(defn draw-triangular-tiling-p3m1-live []
  (q/fill 0 0 0)
  (let [height 20
        width 20]
    (let [x0 (* (/ width 1) @column)
          y0 (* (/ height 2) @row)
          x1 (+ (/ width 2) x0)
          y1 (+ (/ width 2) y0)
          x2 (+ x0 width)
          y2 y0]
      (q/triangle x0 y0 x1 y1 x2 y2))
    (if (< @column (/ (q/width) width))
      (swap! column inc)
      (do
        (swap! row inc)
        (reset! column 0)))))

;; https://en.wikipedia.org/wiki/Triangular_tiling
(defn draw-triangular-tiling-p3m1-not-live []
  (q/background 255 255 255)
  (q/fill 0 0 0)
  (let [height 20
        width 20]
    (dotimes [r (/ (q/height) 2)]
      (dotimes [c (/ (q/width) 2)]
        (let [x0 (* (/ width 1) c)
              y0 (* (/ height 2) r)
              x1 (+ (/ width 2) x0)
              y1 (+ (/ width 2) y0)
              x2 (+ x0 width)
              y2 y0]
          (q/triangle x0 y0 x1 y1 x2 y2))))))

(defn draw-random-from-triangular-tiling-not-live []
  (let [max-height 10
        max-width 10
        colors (random-color 20)]
    (dotimes [r (/ (q/height) 4)]
      (dotimes [c (/ (q/width) 4)]
        (dotimes [i 2]
          (let [width (q/random 2 max-width)
                height (q/random 2 max-height)
                x0 (* (/ width 2) c)
                y0 (* (/ height 2) r)
                x1 (+ (/ width 2) x0)
                y1 (+ (/ width 2) y0)
                x2 (+ x0 width)
                y2 y0]
            (apply q/fill (nth colors (q/random (min (count colors) (+ 0 (quot (* (max r 40) (max c 80)) (q/random 500 1500)))))))
            (q/with-rotation [(q/random (* -1 (/ q/PI 2)) (/ q/PI 2))]
              (q/triangle x0 y0 x1 y1 x2 y2))))))))

(q/defsketch Triangular-Tiling
  :size [1800 1000]
  :title "Triangular-tiling"
  :setup draw-random-from-triangular-tiling-live
  :draw draw-random-from-triangular-tiling-live)

(defn -main [& args])
