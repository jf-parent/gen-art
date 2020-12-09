(ns percolation.percolation-02
  (:require
   [quil.middleware :as m]
   [util.color :as c]
   [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 60)
  (q/background 255))

(defn draw []
  (q/stroke-weight 0)
  (let [size 100
        colors [
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                ]]
    (dotimes [x (inc (/ (q/width) size))]
      (dotimes [y (inc (/ (q/height) size))]
        (apply q/fill (c/random-nippon-color))
        (q/rect (* size x) (* size y) size size)))))

(q/defsketch percolation-02
  :title "Percolation 02"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [3800 1400])

(defn -main [& args])
