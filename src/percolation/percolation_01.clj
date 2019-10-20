(ns percolation.percolation-01
  (:require [quil.middleware :as m]
            [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 120))

(defn draw []
  ;;(q/stroke 0)
  (q/stroke-weight 0)
  (q/fill (q/random 255) (q/random 255) (q/random 255))
  (let [x1 (int (q/random (q/width)))
        y1 (int (q/random (q/height)))]
    (q/rect (* 5 x1) (* 5 y1) 5 5)))

(q/defsketch percolation-01
  :title "Percolation 01"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [800 800])

(defn -main [& args])
