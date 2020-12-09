(ns percolation.percolation-06
  (:require [quil.middleware :as m] [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 120)
  (q/background 255)
  (def size (atom 100))
  (def init (atom 0))
  (def colors [
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               ]))

(defn draw []
  (when (= @init 1)
    (let [cur-size @size
          pixels (q/pixels)]
      (dotimes [x (/ (q/width) cur-size)]
        (dotimes [y (/ (q/height) cur-size)]
          (let [pixel (aget pixels (+ (* y cur-size (* cur-size (/ (q/width) cur-size))) (* x cur-size)))]
            (if (>= (q/random 10) 5)
                    (q/fill (q/color (+ (q/red pixel) 5) (+ (q/green pixel) 5) (+ (q/blue pixel) 5)))
                    (q/fill (q/color (- (q/red pixel) 5) (- (q/green pixel) 5) (- (q/blue pixel) 5))))
            (q/rect (* cur-size x) (* cur-size y) cur-size cur-size)
            )))))
  (when-not (> @init 0)
    (swap! init inc)
    (let [cur-size @size]
      (dotimes [x (/ (q/width) cur-size)]
        (dotimes [y (/ (q/height) cur-size)]
          (apply q/fill (nth colors (int (q/random 0 7))))
          (q/rect (* cur-size x) (* cur-size y) cur-size cur-size))))))

(q/defsketch percolation-06
  :title "Percolation 06"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [3800 1500])

(defn -main [& args])
