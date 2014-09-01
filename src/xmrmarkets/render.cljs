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
  (let [server-ch (<! (ws-ch "ws://localhost:8080/ws"{:format :json-kw}))]
    (go-loop []
      (prn (<! server-ch))
      (recur))
    (>! server-ch "hello lol")))

(comment
(def ws (js/WebSocket. "ws://localhost:8080/ws"))

(defn onOpen []
  (.send ws "lolgos")
  (.log js/console (.-readyState ws)))

(defn onMessage [m]
  (.log js/console m))

(set! (.-onopen ws) onOpen)
(set! (.-onmessage ws) onMessage)
)
