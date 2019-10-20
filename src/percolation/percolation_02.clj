(ns percolation.percolation-02
  (:require [quil.middleware :as m] [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 1)
  (q/background 255))

(defn draw []
  (q/stroke-weight 0)
  (let [size (int (q/random 1 20))
        colors [
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                ]]
    (dotimes [x (inc (/ (q/width) size))]
      (dotimes [y (inc (/ (q/height) size))]
       (apply q/fill (nth colors (int (q/random 0 5))))
       (q/rect (* size x) (* size y) size size)))))

(q/defsketch percolation-02
  :title "Percolation 02"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [1200 1200])

(defn -main [& args])
