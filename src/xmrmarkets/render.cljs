(ns xmrmarkets.render
(:require [chord.client :refer [ws-ch]]
          [cljs.core.async :refer [chan <! >! put! close! timeout]]
          [quiescent :as q :include-macros true]
          [ajax.core :refer [GET]]
          [quiescent.dom :as d])
(:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(.add (.-tz js/moment) "Etc/UTC|UTC|0|0|")

(q/defcomponent TickerCurrency []
  (d/div {:className "ticker-currency"} "BTC/XMR"))

(q/defcomponent TickerPrice [t]
  (d/div {:className "ticker-price"} t))

(q/defcomponent Ticker [t]
  (d/div {:className "ticker"}
         (TickerPrice t) (TickerCurrency)))

(q/defcomponent HistoryItem [h]
  (d/div {:className "history-item "  }
         (d/div {:className "history-item-xmr"} (h "amount"))
         (d/div {:className "history-item-time"} (h "date"))
         (d/div {:className "history-item-price"} (h "rate"))
         (d/div {:className "history-item-updown"} (d/i {:className (str "fa fa-caret-" (h "up-down") " " (h "up-down") )}))))

(q/defcomponent History [h]
  (apply d/div {:className "history-container"}
         (defn add-down-up [v i]
           (let [up-down (if (= (first v) null) "" (if (>= (i "rate") ((first v) "rate")) "up" "down"))]
             (cons (assoc i "up-down" up-down) v)))
         (let [sort (reduce add-down-up [] (reverse h))]
           (map HistoryItem (take 19 sort)))))

(q/defcomponent PriceInfo [d]
  (d/div {}
         (Ticker ((d "ticker") "last"))
         (History (d "history"))))


(def periodlist (list "6h" "24h" "2d" "4d" "1w" "2w" "1m" "all"))

(defn chart-ajax-handler [response]
  (.buildChart (.-XMR js/window) (.parse js/JSON (str response))))

(defn render-chart-control [selected-period] (q/render (ChartControl selected-period) (.getElementById js/document "chart-control")))

(defn on-chart-period-click [period]
  (fn []
    (render-chart-control period)
    (GET (str "chart/" period "/")
         {:handler chart-ajax-handler})))

(q/defcomponent ChartControlItem [selected-period period]
  (d/a {:className (str "chartmenu " (when (= selected-period period) "selected-period"))
        :onClick (on-chart-period-click period)} (str period " ")))

(q/defcomponent ChartControl [selected-period]
  (apply d/div {}
         (map (fn [in] (ChartControlItem selected-period in)) periodlist)))

(enable-console-print!)

(render-chart-control "24h")

(GET (str "chart/24h/") {:handler chart-ajax-handler})

(go
  (let [server-ch (<! (ws-ch "ws://jakoblind.se/xmr/ws"{:format :edn}))
        container (.getElementById js/document "main")]
    (go-loop []
      (when-let [d (:message (<! server-ch))]
        (set! (.-title js/document) (str ((d "ticker") "last") " BTC/XMR"))
        (q/render (PriceInfo d) container)
        (recur)))))
