(ns quil-workflow.core
  (:require [quil.middleware :as m]
            [quil.core :as q]
            [quil-workflow.dynamic :as dynamic]))

(q/defsketch example
  :title "Art War"
  :setup dynamic/setup
  :draw dynamic/draw1
  :middleware [m/pause-on-error]
  :size [1200 1200])
