(ns quil-workflow.dynamic
  (:require [quil.core :as q]))

;; https://github.com/quil/quil/wiki/Dynamic-Workflow-%28for-REPL%29

;; (use 'quil-workflow.core)
;; (use :reload 'quil-workflow.dynamic)

(defn setup []
  (q/smooth)
  (q/stroke 0)
  (q/no-stroke)
  (q/frame-rate 1)
  (q/background 255)
  (def size (atom 10))
  (def init (atom 0))
  (q/set-state! :image-loaded false
                :barbara-image (q/load-image "/Users/jean-francoisparent/Documents/PROG/LISP/war-art/src/quil_workflow/images/barbara.png")
                :image (q/load-image "/Users/jean-francoisparent/Documents/PROG/LISP/war-art/src/quil_workflow/images/black-red.png"))
  (def colors [
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               [(q/random 255) (q/random 255) (q/random 255)]
               ])) ;;use state

(defn draw0 []
  ;;(q/stroke 0)
  (q/stroke-weight 0)
  (q/fill (q/random 255) (q/random 255) (q/random 255))
  (let [x1 (int (q/random 1020))
        y1 (int (q/random 1200))]
    (q/rect (* 5 x1) (* 5 y1) 5 5)))

(defn draw1 []
  (q/stroke-weight 0)
  (let [size (int (q/random 1 20))
        colors [
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                [(q/random 255) (q/random 255) (q/random 255)]
                ]]
    (dotimes [x (inc (/ 1200 size))]
      (dotimes [y (inc (/ 1200 size))]
       (apply q/fill (nth colors (int (q/random 0 5))))
       (q/rect (* size x) (* size y) size size)))))

(defn draw2 []
  (q/stroke-weight 0)
  (let [size 5]
    (dotimes [x (inc (/ 1200 size))]
      (dotimes [y (inc (/ 1200 size))]
        (when (> (q/random 0 5) 4)
          (apply q/fill (nth colors (int (q/random 0 5))))
          (q/rect (* size x) (* size y) size size))))))

(defn draw3 []
  (q/stroke-weight 0)
  (let [cur-size @size]
    (when (> cur-size 0)
      (dotimes [x (inc (/ 1200 cur-size))]
        (dotimes [y (inc (/ 1200 cur-size))]
          (when (> (q/random 0 5) 4)
            (apply q/fill (nth colors (int (q/random 0 5))))
            (q/rect (* cur-size x) (* cur-size y) cur-size cur-size))))))
  (swap! size dec))

(defn draw4 []
  (q/stroke-weight 0)
  (let [cur-size @size]
    (when (> cur-size 0)
      (dotimes [x (inc (/ 1200 cur-size))]
        (dotimes [y (inc (/ 1200 cur-size))]
          (apply q/fill (nth colors (int (q/random 0 5))))
          (q/rect (* cur-size x) (* cur-size y) cur-size cur-size)))))
  (swap! size dec))

(defn draw5 []
  (when-not (q/state :image-loaded)
    (dotimes [x 100]
      (dotimes [y 100]
        (q/image (q/state :image) (* x 100) (* y 100))))
    (q/set-state! :image-loaded true))
  (let [pixels (q/pixels)]
    (dotimes [i (* 1000 1000)]
      (let [pixel (aget pixels i)]
        (aset-int pixels i (q/color (+ (q/red pixel) (q/random 10)) (+ (q/green pixel) (q/random 10)) (+ (q/green pixel )(q/random 10)))))))
  (q/update-pixels))

(defn draw6 []
  (when-not (q/state :image-loaded)
    (q/image (q/state :barbara-image) 0 0)
    (q/set-state! :image-loaded true))
  (let [pixels (q/pixels)]
    (dotimes [i (* (q/height) (q/width))]
      (let [pixel (aget pixels i)]
        (when (and (>= (q/red pixel) 10) (>= (q/green pixel) 50) (<= (q/blue pixel) 150))
          (aset-int pixels i (q/color (+ (q/red pixel) (q/random -50 50)) (+ (q/green pixel) (q/random -50 50)) (+ (q/blue pixel) (q/random -50 50)))))))
    (q/update-pixels)))

(defn draw7 []
  (let [pixels (q/pixels)]
    (aset-int pixels 0 (q/color 125 125 125))
    (print (q/color 125 125 125))
    (q/update-pixels)
    (print (aget pixels 0))))

(defn draw8 []
  (when (= @init 1)
    (let [cur-size @size
          pixels (q/pixels)]
      (dotimes [x (/ (q/width) cur-size)]
        (dotimes [y (/ (q/height) cur-size)]
          (let [pixel (aget pixels (+ (* y cur-size (* cur-size (/ (q/width) cur-size))) (* x cur-size)))]
            ;;(println (+ (* x cur-size (* cur-size (/ (q/width) cur-size))) (* y cur-size)))
            ;;(println pixel)
            (if (>= (q/random 10) 1)
                    (q/fill (q/color (+ (q/red pixel) 25) (+ (q/green pixel) 25) (+ (q/blue pixel) 25)))
                    (q/fill (q/color (- (q/red pixel) 25) (- (q/green pixel) 25) (- (q/blue pixel) 25))))
            (q/rect (* cur-size x) (* cur-size y) cur-size cur-size)
            )))))
  (when-not (> @init 0)
    (swap! init inc)
    (let [cur-size @size]
      (dotimes [x (/ (q/width) cur-size)]
        (dotimes [y (/ (q/height) cur-size)]
          (apply q/fill (nth colors (int (q/random 0 7))))
          (q/rect (* cur-size x) (* cur-size y) cur-size cur-size)))))
  )
