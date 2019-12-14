(ns quil-workflow.dynamic
  (:require [quil.core :as q]
            [me.raynes.fs :as fs]))

;; https://github.com/quil/quil/wiki/Dynamic-Workflow-%28for-REPL%29

;; (use 'quil-workflow.core)
;; (use :reload 'quil-workflow.dynamic)

(set! *warn-on-reflection* true)

(defn random-color [nb]
  (take nb (repeatedly (fn [] [(q/random 255) (q/random 255) (q/random 255)]))))

(defn setup [])

(defn draw [])

(q/defsketch Prototyping
  :size [1800 1000]
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
