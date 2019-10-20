(ns percolation.percolation-04
  (:require [quil.middleware :as m] [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 1)
  (q/background 255)
  (def size (atom 20))
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
  (let [cur-size @size]
    (when (> cur-size 0)
      (dotimes [x (inc (/ (q/width) cur-size))]
        (dotimes [y (inc (/ (q/height) cur-size))]
          (when (> (q/random 0 5) 4)
            (apply q/fill (nth colors (int (q/random 0 5))))
            (q/rect (* cur-size x) (* cur-size y) cur-size cur-size))))))
  (swap! size dec))

(q/defsketch percolation-04
  :title "Percolation 04"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [1200 1200])

(defn -main [& args])
