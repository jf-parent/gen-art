(ns quil-workflow.core
  (:require [quil.middleware :as m]
            [quil.core :as q]
            [quil-workflow.dynamic :as dynamic]))

(q/defsketch example
  :title "Art War"
  :setup dynamic/setup
  :draw dynamic/draw
  :middleware [m/pause-on-error]
  :size [1280 720])
