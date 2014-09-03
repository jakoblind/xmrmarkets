(ns xmrmarkets.render
(:require [chord.client :refer [ws-ch]]
          [cljs.core.async :refer [chan <! >! put! close! timeout]]
          [quiescent :as q :include-macros true]
          [quiescent.dom :as d])
(:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(q/defcomponent Ticker [t]
  (prn t)
  (apply d/div {} t))

(q/defcomponent HistoryItem [h]
  (apply d/div {} (h "amount") ", " (h "date") ", " (h "rate") ", up or down"))

(q/defcomponent History [h]
  (apply d/div {}
         (defn sort-by-date [m]
           (sort-by #(vec (map % "date")) m))
         (defn date-human-readable [m]
           (map (fn [i] (update-in i ["date"] (fn [a] (.fromNow (js/moment. a "YYYY-MM-DD hh:mm:ss"))))) m))
         (let [sort (sort-by-date (date-human-readable h))]
           (map HistoryItem sort))))

(q/defcomponent PriceInfo [d]
  (d/div {}
         (Ticker ((d "ticker") "last"))
         (History (d "history"))))

(enable-console-print!)

(go
  (let [server-ch (<! (ws-ch "ws://localhost:8080/ws"{:format :edn}))
        container (.getElementById js/document "main")]
    (go-loop []
      (when-let [d (:message (<! server-ch))]
        (q/render (PriceInfo d) container)
        (recur)))))
