(ns xmrmarkets.render
  (:require [quiescent :as q :include-macros true]
	    [quiescent.dom :as d]))

(q/defcomponent Ticker []
  (apply d/div {:className "lolz"}))

(q/render (Ticker) (.getElementById js/document "main"))
