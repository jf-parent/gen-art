(ns quil-workflow.dynamic
  (:require [quil.core :as q]))

;; https://github.com/quil/quil/wiki/Dynamic-Workflow-%28for-REPL%29

;; (use 'quil-workflow.core)
;; (use :reload 'quil-workflow.dynamic)

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 10)
  (q/background 255)
  (def size (atom 5))
  (def init (atom 0))
  (q/set-state! :image (q/load-image "/Users/jean-francoisparent/Documents/PROG/LISP/gen-art/src/quil_workflow/images/image_3.jpg")))

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
