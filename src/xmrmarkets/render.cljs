(ns xmrmarkets.render
(:require [chord.client :refer [ws-ch]]
          [cljs.core.async :refer [chan <! >! put! close! timeout]]
          [quiescent :as q :include-macros true]
          [ajax.core :refer [GET]]
          [xmrmarkets.config :refer [config]]
          [quiescent.dom :as d])
(:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(.add (.-tz js/moment) "Etc/UTC|UTC|0|0|")

(defonce selected-chart-period (atom {:period "4d"}))

(q/defcomponent Ticker [t]
  (d/div {:className "ticker"}
         (d/div {:className "ticker-price"} t) (d/div {:className "ticker-currency"} "BTC/XMR")))

(q/defcomponent HistoryItem [h]
  (d/div {:className "history-item "  }
         (d/div {:className "history-item-xmr"} (h "amount"))
         (d/div {:className "history-item-time"} (h "date"))
         (d/div {:className "history-item-price"} (h "rate"))
         (d/div {:className "history-item-updown"} (d/i {:className (str "fa fa-caret-" (h "up-down") " " (h "up-down") )}))))

(q/defcomponent History [h]
  (apply d/div {:className "history-container"}
           (map HistoryItem (take 23 h))))

(def periodlist (list "6h" "24h" "2d" "4d" "1w" "2w" "1m" "all"))

(defn chart-ajax-handler [response]
  (.buildChart (.-XMR js/window) (.parse js/JSON (str response))))

(defn render-chart-control [selected-period]
  (q/render (ChartControl selected-period)
            (.getElementById js/document "chart-control")))

(defn on-chart-period-click [period]
  (fn []
    (reset! selected-chart-period {:period period})
    (render-chart-control period)
    (GET (str "chart/" period "/")
         {:handler chart-ajax-handler})))

(q/defcomponent ChartControlItem [selected-period period]
  (d/a {:className (str "chartmenu " (when (= selected-period period) "selected-menu"))
        :onClick (on-chart-period-click period)} (str period " ")))

(q/defcomponent ChartControl [selected-period]
  (apply d/div {}
         (map (fn [in] (ChartControlItem selected-period in)) periodlist)))

(enable-console-print!)

(render-chart-control (:period @selected-chart-period))

(defonce latest-updated-chart (atom nil))

(go
   (let [server-ch (<! (ws-ch (:ws-url config) {:format :edn}))
         pricecontainer (.getElementById js/document "pricecontainer")
         tickercontainer (.getElementById js/document "tickercontainer")]
     (go-loop []
       (>! server-ch {:period (:period @selected-chart-period) :ts @latest-updated-chart})
       (when-let [msg (:message (<! server-ch))]
         (let [t (:ticker msg)
               h (:chart-history msg)]
           (when (not= h nil)
             (let [ts (:timestamp h)
                   chart (:history h)]
               (reset! latest-updated-chart ts)
               (.buildChart (.-XMR js/window) (.parse js/JSON (str chart)))))
           (set! (.-title js/document) (str (t "ticker")  " BTC/XMR"))
           (q/render (History (t "history")) pricecontainer)
           (q/render (Ticker (t "ticker")) tickercontainer))
         (recur)))))
