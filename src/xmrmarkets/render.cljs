(ns xmrmarkets.render
(:require [chord.client :refer [ws-ch]]
	  [cljs.core.async :refer [chan <! >! put! close! timeout]]
	  [quiescent :as q :include-macros true]
	  [quiescent.dom :as d])
(:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(q/defcomponent Ticker []
  (apply d/div {:className "lolz"} "lolz"))

(q/render (Ticker) (.getElementById js/document "main"))

(enable-console-print!)

(go
  (let [server-ch (<! (ws-ch "ws://localhost:8080/ws"{:format :edn}))]
    (go-loop []
      (prn (<! server-ch))
      (recur))))
