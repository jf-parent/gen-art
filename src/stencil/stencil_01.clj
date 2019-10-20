(ns stencil.stencil-01
  (:require [quil.middleware :as m]
            [quil.core :as q]))

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 30)
  (q/background 255)
  (def init (atom 0))
  (q/set-state! :stencil-image (q/load-image "/Users/jean-francoisparent/Documents/PROG/LISP/war-art/src/stencil/images/stencil-01.jpg")))

(defn draw []
  (when (= @init 1)
    (let [pixels (q/pixels)]
      (dotimes [i (* (q/height) (q/width))]
        (let [pixel (aget pixels i)]
          (when (<= (int (+ (q/red pixel) (q/green pixel) (q/blue pixel))) 500)
            (aset-int pixels i (q/color (+ (q/red pixel) 5) (q/green pixel) (q/blue pixel)))
      (q/update-pixels))))))
  (when-not (> @init 0)
    (swap! init inc)
    (q/image (q/state :stencil-image) 0 0)))

(q/defsketch stencil-01
  :title "Stencil 01"
  :setup setup
  :draw draw
  :middleware [m/pause-on-error]
  :size [200 200])
