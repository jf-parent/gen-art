(ns blending.blending
  (:require [quil.core :as q]
            [me.raynes.fs :as fs]))

(defn setup []
  (q/background 0 0 0)
  (q/frame-rate 60)
  (q/set-state! :images (fs/list-dir (str fs/*cwd* "/src/blending/images"))))

(defn draw []
  (let [modes [
               :blend :lightest :difference
               :exclusion :multiply :overlay
               :screen :soft-light]
        images (q/state :images)
        eff-image (q/load-image (fs/absolute (nth images (q/random (count images)))))
        eff-mode (nth modes (q/random (count modes)))]
    (q/blend
     eff-image
     (q/random 250)
     (q/random 250)
     (q/random (q/width))
     (q/random (q/height))
     (q/random 1000)
     (q/random 1000)
     (q/random 1000)
     (q/random 1000)
     eff-mode)))

(q/defsketch Blending
  :size [1800 1000]
  :title "Blending"
  :setup setup
  :draw draw)

(defn -main [& args])
