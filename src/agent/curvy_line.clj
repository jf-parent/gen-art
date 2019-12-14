(ns agent.curvy-line
  (:require [quil.core :as q]))

;; Thanks to Karsten Schmidt
;; https://gist.github.com/postspectacular/5257574

(set! *warn-on-reflection* true)

(def num-agents 5000)
(def num-targets 50)

(def app-state (atom {}))

(defn mag2
  "2d vector magnitude"
  [[^double x ^double y]] (Math/sqrt (+ (* x x) (* y y))))

(defn normalize2
  "2d vector normalize"
  [v]
  (let [m (/ 1.0 (+ (mag2 v) 1e-6))]
    [(* (v 0) m) (* (v 1) m)]))

(defn scale2-n
  "Uniform 2d vector scale"
  [v ^double s]
  [(* s (v 0)) (* s (v 1))])

(defn vop2
  "Generic 2d vector operation. Applies f to a b componentwise."
  [f a b]
  [(f (a 0) (b 0)) (f (a 1) (b 1))])

(defn random-point-in-rect
  [w h]
  [(q/random w) (q/random h)])

(defn make-target
  "Returns a map specifying a random target pos & radius."
  [w h]
  {:pos (random-point-in-rect w h)
   :radius (q/random 10.0 20.0)})

(defn make-agent
  "Returns a map specifying a randomly configured agent and
  picks a random target from the given list of targets."
  [w h targets]
  (let [pos (random-point-in-rect w h)]
    {:pos pos
     :prev pos
     :dir [(q/random -1 1) (q/random -1 1)]
     :speed (q/random 1 5)
     :steer (q/random 0.025 0.1)
     :target (-> targets count q/random int targets)
     :col (repeatedly 3 #(q/random 255))}))

(defn wrap-coord ^double [^double x ^double x1 ^double x2 ^double r]
  "Wraps `x` on both ends `x1`/`x2` with radius `r`."
  (if (< x (- x1 r))
    (+ x2 r)
    (if (> x (+ x2 r))
      (- x1 r)
      x)))

(defn steer-towards
  "Steers `dir` towards `target` with `steer` strength.
  Normalizes both input and result vector(s)."
  [target dir ^double steer]
  (->> target
    (normalize2)
    (vop2 #(+ % (* (- %2 %) steer)) dir)
    (normalize2)))

(defn update-agent
  "If not yet reached target, updates an agent's direction & position.
  Returns updated agent state map."
  [{:keys [pos dir speed steer] {tpos :pos tradius :radius} :target :as agent}]
  (let [delta (vop2 - tpos pos)]
    (if (> (mag2 delta) tradius)
      (let [dir (steer-towards delta dir steer)
            scaled-dir (scale2-n dir speed)
            new-pos (vop2 + pos scaled-dir)]
        (assoc agent :pos new-pos :prev pos :dir dir))
      agent)))

(defn random-targets
  "Returns a vector of `n` random targets."
  [n w h]
  (vec (repeatedly n #(make-target w h))))

(defn random-agents
  "Returns a lazyseq of `na` random agents, each with a random
  target ID between (0 .. `nt`)."
  [na w h targets]
  (repeatedly na #(make-agent w h targets)))

(defn restart-sim
  "Clears canvas, replaces the current set of agents & targets with
  new random instances."
  []
  (q/background 255)
  (let [targets (random-targets num-targets (q/width) (q/height))
        agents (random-agents num-agents (q/width) (q/height) targets)]
    (swap! app-state assoc
           :targets targets
           :agents agents)))

(defn setup
  "Initializes viewport and app state map (agents & targets)."
  []
  (q/ellipse-mode :radius)
  (q/no-fill)
  (restart-sim))

(defn draw
  "Updates all agents, then draws them (and targets too)."
  []
  (let [{:keys [agents targets]} @app-state
        ;; if num-agents is very large or `update-agent` becomes
        ;; more complex, it might make sense to switch to from
        ;; `map` to `pmap` for parallel execution
        ;; also see: http://clojuredocs.org/clojure_core/clojure.core/pmap
        agents (pmap update-agent agents)]
    (swap! app-state assoc :agents agents)
    (doseq [{[x y] :pos [px py] :prev [r g b] :col} agents]
      (q/stroke r g b)
      (q/line px py x y))
    (q/stroke 0)
    (q/no-fill)
    ;;(doseq [{[x y] :pos r :radius} targets]
    ;;  (ellipse x y r r))
    ;;(q/fill 255)
    ;;(q/rect 0 0 100 20)
    ;;(q/fill 0)
    #_(q/text (str (q/current-frame-rate)) 10 16)))

(q/defsketch Agents
  :size [1200 800]
  ; :renderer :opengl
  :title "Quil agents"
  :setup setup
  :draw draw
  :mouse-pressed restart-sim)

(defn -main [& args])
