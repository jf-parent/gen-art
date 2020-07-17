(ns quil-workflow.dynamic
  (:require [quil.core :as q]
            [clojure.string :as cs]
            [me.raynes.fs :as fs]))

;; https://github.com/quil/quil/wiki/Dynamic-Workflow-%28for-REPL%29

;; (use 'quil-workflow.core)
;; (use :reload 'quil-workflow.dynamic)

(set! *warn-on-reflection* true)

(def current-formula (atom ""))
(def current-x (atom 100))
(def current-y (atom 100))

(defn l-system-derive [formula rules steps]
  (loop [f formula step steps]
    (if (<= step 0)
      f
      (recur (cs/join #"" (mapcat #((keyword %) rules) (cs/split f #""))) (dec step)))))

(defn random-color [nb]
  (take nb (repeatedly (fn [] [(q/random 255) (q/random 255) (q/random 255)]))))

(defn setup []
  (q/background 255 255 255)
  (q/frame-rate 1)
  (reset! current-formula (cs/split (l-system-derive "X" {:X "-YF+XFX+FY-" :Y "+XF-YFY-FX+"} 3) #""))
  (println @current-formula))


(defn draw []
  (let [c (first @current-formula) angle 90 x @current-x y @current-y length 10]
    (when (seq c)
      (reset! current-formula (rest @current-formula))
      (when (= c "F")
        (reset! current-x (+ x length))
        (q/line x y @current-x y)
        (println "forward!"))
      (when (= c "+")
        (reset! current-y (+ y length))
        (q/line x y x @current-y)
        (println "Turn angle right!"))
      (when (= c "-")
        (reset! current-y (- y length))
        (q/line x y x @current-y)
        (println "Turn angle left!"))
      (println c x y))))

(q/defsketch Prototyping
  :size [500 500]
  :title "Prototyping"
  :setup setup
  :draw draw)

;; Agents
;; (def img-path "/Users/jean-francoisparent/Documents/PROG/LISP/gen-art/src/flickering/images/image.jpg")
;; (def num-agents 100)
;; (def size 1)

;; (def app-state (atom {}))

;; (defn make-agent [x0 x1]
;;   {:x0 x0 :x1 x1})

;; (defn update-agent [agent]
;;   (let [pixels (q/pixels)
;;         x (q/random (q/width))
;;         y (q/random (q/height))
;;         pixel (aget pixels (+ y (* x (q/width))))]))

;; (defn setup []
;;   (q/no-fill)
;;   (q/stroke 0)
;;   (q/no-stroke)
;;   (q/frame-rate 20)
;;   (q/background 255)
;;   (q/set-state! :image (q/load-image img-path))
;;   (q/image (q/state :image) 0 0)
;;   ;;(q/resize-sketch (. (q/state :image) width) (. (q/state :image) height))
;;   (let [line-by-agent (int (/ (q/width) num-agents))
;;         agents (vec (for [i (range num-agents)]
;;                   (make-agent (* i line-by-agent) (* (+ i 1) line-by-agent))))]
;;     (print agents)
;;     (swap! app-state assoc :agents agents)))

;; (defn draw []
;;   (pmap update-agent (:agents @app-state)))

;; (q/defsketch Agents
;;   :size [500 500]
;;   :title "Agent Flickering"
;;   :setup setup
;;   :draw draw)
