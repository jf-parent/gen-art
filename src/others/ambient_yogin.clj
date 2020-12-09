(ns geometric.boxed-squares
  (:require [quil.middleware :as m]
            [quil.core :as q]
            [me.raynes.fs :as fs]
            [util.color :as c]))

(def x (atom 200))
(def y (atom 200))
(def size (atom 0))
(def max-size 100)
(def angle (atom 0))
(def mode (atom 0))
(def tick (atom -1))
(def tick-mode 1000)

(defn warhol-img [blend?]
  (let [img-paths [(str fs/*cwd* "/src/others/yogin.jpg")
                   (str fs/*cwd* "/src/others/yogin_bw.jpg")
                   (str fs/*cwd* "/src/others/yogin_invert.jpg")]]
    (dotimes [i 4]
     (dotimes [j 3]
       (let [modes [:difference :overlay :blend]
             img-path (nth img-paths (q/random 3))
             img (q/load-image img-path)]
         (if blend?
           (q/blend img 0 0 1000 1000 (* i 500) (* j 400) 500 400 (nth modes (q/random (count modes))))
           (q/image img (* i 500) (* j 400))))))))

(defn setup []
  (q/stroke 255 255 255)
  (q/background 0 0 0)
  (q/no-fill)
  (warhol-img false)
  (q/frame-rate 60))

(defn draw-square [x y s]
  (q/rect x y s s))

(defn draw-circle [x y s]
  (q/ellipse x y s s))

(defn draw []
  (when (zero? (mod @tick tick-mode))
    (warhol-img true))
  (let [img-paths [(str fs/*cwd* "/src/others/yogin.jpg")
                   (str fs/*cwd* "/src/others/yogin_bw.jpg")
                   (str fs/*cwd* "/src/others/yogin_invert.jpg")]
        img-path (nth img-paths (q/random 3))
        img (q/load-image img-path)]
    (q/blend img 0 0 1000 1000 1500 800 500 400 :difference)
    (q/blend img 0 0 1000 1000 500 400 500 400 :difference))
  (swap! tick inc)
  (dotimes [_ 5]
    (let [new-x (mod (+ (q/random 1) @x 0) (- (q/width) 100))
         new-y (mod (+ (q/random 1) @y 0) (- (q/height) 100))
         new-angle (+ @angle (/ q/PI 200))
         new-size (mod (+ (q/random 25) @size 0) max-size)]
      (if (>= (q/random 10) 5)
        (q/stroke 255 255 255)
        (q/stroke 0 0 0))
      (q/with-translation [1500 800]
       (q/with-rotation [(* 1 new-angle)]
         (if (>= (q/random 10) 5)
           (draw-circle 0 0 new-size)
           (draw-square 0 0 new-size))))
      (q/with-translation [500 400]
       (q/with-rotation [new-angle]
         (if (>= (q/random 10) 5)
           (draw-circle 0 0 new-size)
           (draw-square 0 0 new-size))))
      (q/with-translation [1000 400]
       (q/with-rotation [(* -1 new-angle)]
         (if (>= (q/random 10) 5)
           (draw-circle 0 0 new-size)
           (draw-square 0 0 new-size))))
      (q/with-translation [500 800]
       (q/with-rotation [(* -1 new-angle)]
         (if (>= (q/random 10) 5)
           (draw-circle 0 0 new-size)
           (draw-square 0 0 new-size))))
      (apply q/stroke (c/random-swiss-color))
      (q/with-translation [1000 800]
       (q/with-rotation [(* 1 new-angle)]
         (if (>= (q/random 10) 5)
           (draw-circle 0 0 new-size)
           (draw-square 0 0 new-size))))
      (q/with-translation [1500 400]
       (q/with-rotation [(* -1 new-angle)]
         (if (>= (q/random 10) 5)
           (draw-circle 0 0 new-size)
           (draw-square 0 0 new-size))))
     ;; (q/with-translation [new-x new-y]
     ;;   (q/with-rotation [new-angle]
     ;;     (draw-square 0 0 new-size)))
     (reset! x new-x)
     (reset! y new-y)
     (reset! size new-size)
     (reset! angle new-angle))))

(q/defsketch Ambient-Yogin
  :size [2000 1200]
  :title "Ambient Yogin"
  :setup setup
  :draw draw)

(defn -main [& args])
