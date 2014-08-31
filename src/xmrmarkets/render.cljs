(ns xmrmarkets.render
  (:require [quiescent :as q :include-macros true]
	    [quiescent.dom :as d]))

(q/defcomponent Ticker []
  (apply d/div {:className "lolz"} "lolz"))

(q/render (Ticker) (.getElementById js/document "main"))

(def ws (js/WebSocket. "ws://localhost:8080/w"))

(.log js/console (.-getReady ws))
