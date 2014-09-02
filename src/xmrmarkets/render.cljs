(ns xmrmarkets.render
(:require [chord.client :refer [ws-ch]]
	  [cljs.core.async :refer [chan <! >! put! close! timeout]]
	  [quiescent :as q :include-macros true]
	  [quiescent.dom :as d])
(:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(q/defcomponent Ticker [t]
  (prn t)
  (apply d/div {:className "lolz"} (get t "last")))


(enable-console-print!)

(go
  (let [server-ch (<! (ws-ch "ws://localhost:8080/ws"{:format :edn}))
	container (.getElementById js/document "main")]
    (go-loop []
      (when-let [t (:message (<! server-ch))]
	(q/render (Ticker t) container)
	(recur)))))
