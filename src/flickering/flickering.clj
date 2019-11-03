(ns flickering.flickering
  (:require [quil.middleware :as m]
            [quil.core :as q :include-macros true]))

;; lein run -m flickering.flickering <full-image-path>
(def img-path (atom nil))

(defn setup []
  (println @img-path)
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 10)
  (q/background 255)
  (def size (atom 5))
  (def init (atom 0))
  (q/set-state! :image (q/load-image @img-path))
  (q/resize-sketch (. (q/state :image) width) (. (q/state :image) height)))


(defn draw []
  (when (= @init 0)
    (swap! init inc)
    (q/image (q/state :image) 0 0))
  (when (= @init 1)
    (swap! init inc)
    (let [pixels (q/pixels)]
      (dotimes [x (/ (q/width) @size)]
        (dotimes [y (/ (q/height) @size)]
          (let [local-pixels (transient [])
                avg-red (atom 0)
                avg-green(atom 0)
                avg-blue(atom 0)]
            (dotimes [i @size]
              (dotimes [j @size]
                (conj! local-pixels (aget pixels (+ (* y @size (* @size (/ (q/width) @size))) (* x @size))))))
            (let [lp (persistent! local-pixels)]
              (reset! avg-red (/ (reduce + (into [] (map #(q/red %) lp))) (* @size @size)))
              (reset! avg-green (/ (reduce + (into [] (map #(q/green %) lp))) (* @size @size)))
              (reset! avg-blue (/ (reduce + (into [] (map #(q/blue %) lp))) (* @size @size))))
            (q/fill (q/color @avg-red @avg-green @avg-blue))
            (q/rect (* @size x) (* @size y) @size @size))))))
  (when (= @init 2)
    (let [pixels (q/pixels)]
      (dotimes [x (/ (q/width) @size)]
        (dotimes [y (/ (q/height) @size)]
          (let [pixel (aget pixels (+ (* y @size (* @size (/ (q/width) @size))) (* x @size)))]
            (if (>= (q/random 10) 5)
              (q/fill (q/color (+ (q/red pixel) 5) (+ (q/green pixel) 5) (+ (q/blue pixel) 5)))
              (q/fill (q/color (- (q/red pixel) 5) (- (q/green pixel) 5) (- (q/blue pixel) 5))))
            (q/rect (* @size x) (* @size y) @size @size)))))))



(defn -main [& args]
  (reset! img-path (first args))
  (q/defsketch flickering
    :title "Flickering"
    :setup setup
    :draw draw
    :size [100 100]))
