(ns percolation.percolation-03
  (:require [quil.middleware :as m] [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 1)
  (q/background 255)
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
  (q/stroke-weight 0)
  (let [size 5]
    (dotimes [x (inc (/ 1200 size))]
      (dotimes [y (inc (/ 1200 size))]
        (when (> (q/random 0 5) 4)
          (apply q/fill (nth colors (int (q/random 0 5))))
          (q/rect (* size x) (* size y) size size))))))

(q/defsketch percolation-03
  :title "Percolation 03"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [1200 1200])

(defn -main [& args])
