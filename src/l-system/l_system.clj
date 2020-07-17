(ns l-system.l-system
  (:refer-clojure :exclude [repeat])
  (:require [quil.core :as q]
            [clojure.string :as cs])
     (:import java.util.Date))

;; Adapted from https://github.com/google/clojure-turtle

(def DEFAULT-COLOR [255 255 255])
(def DELAY-DRAWING 0)

(defn l-system-derive [formula rules steps]
  (loop [f formula step steps]
    ;;(println f step)
    (if (<= step 0)
      f
      (recur (cs/join #"" (mapcat #((keyword %) rules %) (cs/split f #""))) (dec step)))))

(defrecord Turtle [x y angle pen color fill config commands] Object
  (toString [turt] (str (select-keys turt (keys turt)))))

(defn pr-str-turtle [turt]
  (pr-str
    (letfn [(format-key [key]
                        {key (float (/ (bigint (* (get turt key) 10)) 10))})]
      (merge (select-keys turt [:pen :color :fill])
             (format-key :x)
             (format-key :y)
             (format-key :angle)))))

(defmethod print-method Turtle [turt writer]
          (.write writer (pr-str-turtle turt)))

(defn new-turtle []
  (atom (map->Turtle {:x 0
                      :y 0
                      :angle 90
                      :pen true
                      :color DEFAULT-COLOR
                      :fill false
                      :config {}
                      :commands []})))

(def turtle (new-turtle))

(defn alter-turtle [f]
  (swap! turtle f)
  turtle)

(defn color [c]
  (letfn [(alter-fn [t] (-> t
                            (assoc :color c)
                            (update-in [:commands] conj [:color c])))]
    (alter-turtle alter-fn)))

(defn right [ang]
  (letfn [(add-angle
            [{:keys [angle] :as t}]
            (let [new-angle (-> angle
                                (- ang)
                                (mod 360))]
              (-> t
                  (assoc :angle new-angle)
                  (update-in [:commands] conj [:setheading new-angle]))))]
    (alter-turtle add-angle)))

(defn left [ang]
  (right (* -1 ang)))

(defn forward [len]
  (let [rads (q/radians (get @turtle :angle))
        dx (* len (Math/cos rads))
        dy (* len (Math/sin rads))
        alter-fn (fn [t] (-> t
                             (update-in [:x] + dx)
                             (update-in [:y] + dy)
                             (update-in [:commands] conj [:translate [dx dy]])))] 
    (alter-turtle alter-fn)))

(defn back [len]
  (forward (* -1 len)))

(defn penup []
  (letfn [(alter-fn [t] (-> t
                            (assoc :pen false)
                            (update-in [:commands] conj [:pen false])))]
    (alter-turtle alter-fn)))

(defn pendown []
  (letfn [(alter-fn [t] (-> t
                            (assoc :pen true)
                            (update-in [:commands] conj [:pen true])))]
    (alter-turtle alter-fn)))

(defn wait [ms]
  (letfn [(get-time [] (.getTime (Date.)))]
    (let [initial-time (get-time)]
      (while (< (get-time) (+ initial-time ms))))))

(defn clean []
  (letfn [(alter-fn
            [t]
            (let [curr-pos-map (select-keys t [:x :y])]
              (-> t
                  (assoc :commands []))))]
    (alter-turtle alter-fn)))

(defn setxy [x y]
  (let [pen-down? (get @turtle :pen)]
    (letfn [(alter-fn [t] 
              (-> t
                  (assoc :x x)
                  (assoc :y y)
                  (update-in [:commands] conj [:setxy [x y]])))]
      (alter-turtle alter-fn))))

(defn setheading [ang]
  (letfn [(alter-fn [t] (-> t
                            (assoc :angle ang)
                            (update-in [:commands] conj [:setheading ang])))]
    (alter-turtle alter-fn)))

(defn reset []
  (clean)
  (new-turtle))

(defn get-fn-or-value [key config]
  (if (fn? (get config key))
    ((get config key))
    (get config key)))

(defn l-system-to-turtle-command [step config]
  (case step
    \- (left (get-fn-or-value :angle config))
    \F (forward (get-fn-or-value :length config))
    \+ (right (get-fn-or-value :angle config))
    :ignore))

(defn setup []
  (q/stroke-weight 1)
  (apply q/stroke DEFAULT-COLOR)
  (apply q/fill DEFAULT-COLOR)
  (q/background 0)
  (q/smooth)
  ;; (print (:config @turtle))
  (setxy (/ (q/width) 2) (/ (q/height) 2)) 
  (let [config (:config @turtle)
        steps (l-system-derive (:w config) (:rules config) (:iteration config))]
    (doseq [step steps]
      (when-let [new-color ((:color-fn config))]
        (color new-color))
      (l-system-to-turtle-command step config)))
  (println turtle))

(defn draw-turtle-commands []
  (dotimes [_ 10]
    (let [commands (:commands @turtle)
          next-cmd (first commands)
          cmd-name (first next-cmd)
          cmd-vals (rest next-cmd)]
      (when-not (empty? commands)
        ;;(println cmd-name cmd-vals (:x @turtle) (:y @turtle))
        (case cmd-name
          :color (let [c (first cmd-vals)]
                    (apply q/stroke c)
                    (apply q/fill c)
                    (swap! turtle assoc :color c))
          :setxy (let [[x y] (first cmd-vals)]
                    (swap! turtle assoc :x x :y y))
          :setheading (swap! turtle assoc :angle (first cmd-vals))
          :translate (let [x (:x @turtle)
                            y (:y @turtle)
                            [dx dy] (first cmd-vals)
                            new-x (+ x dx)
                            new-y (+ y dy)]
                        (when (:pen @turtle)
                          (q/line x y new-x new-y)
                          (when (:fill @turtle)
                            (q/vertex x y)
                            (q/vertex new-x new-y)))
                        (swap! turtle assoc :x new-x :y new-y))
          :pen (swap! turtle assoc :pen (first cmd-vals))
          :error))
      (swap! turtle assoc :commands (rest commands)))))


(defn draw []
  (Thread/sleep DELAY-DRAWING)
  (draw-turtle-commands))

(defmacro new-window [& [config]]
    (let [default-config {:title "Watch the turtle go!"
                           :size [500 500]}
           {:keys [title size]} (merge default-config config)]
       `(q/defsketch ~'example
          :title ~title
          :setup setup
          :draw draw
          :size ~size)))

(defn draw-l-system [config]
  (reset)
  (swap! turtle assoc :config config)
  (new-window {:size [1000 1000] :title (:title config "L-System")}))

(defn hilbert-curve []
  (draw-l-system {:w "X"
                  :rules {:X "-YF+XFX+FY-" :Y "+XF-YFY-FX+"}
                  :iteration 5
                  :angle #(q/random 89 90)
                  :length #(q/random 8 9)
                  :title "Hilbert Curve"
                  :color-fn #(vec [(q/random 255) (q/random 255) (q/random 255)])}))
