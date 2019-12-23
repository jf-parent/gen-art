(ns blending.blending
  (:require [quil.core :as q]
            [me.raynes.fs :as fs]))

(defn setup []
  (q/background 0 0 0)
  (q/frame-rate 60)
  (q/set-state! :images (fs/list-dir (str fs/*cwd* "/src/blending/images"))))


               ;;:add :blend :lightest :difference :blend
               ;;:exclusion :difference :soft-light]
(defn draw []
  (let [modes [
                :difference :difference  :difference  :difference  :difference  :difference 
                :add
              ]
        images (q/state :images)
        eff-image (q/load-image (fs/absolute (nth images (q/random (count images)))))
        eff-mode (nth modes (q/random (count modes)))]
    (q/blend
     eff-image
     0
     0
     (q/width)
     (q/height)
     (q/random 1200)
     (q/random 1200)
     (q/random 300 1200)
     (q/random 300 1200)
     eff-mode)))

(q/defsketch Blending
  :size [1800 1000]
  :title "Blending"
  :setup setup
  :draw draw)

(defn -main [& args])
