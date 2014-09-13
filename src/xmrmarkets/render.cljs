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
         (h "amount") "  " (h "date") " " (h "rate") " "
         (d/i {:className (str "fa fa-caret-" (h "up-down") " " (h "up-down") )})))

(q/defcomponent History [h]
  (apply d/div {:className "history-container"}
         (defn sort-by-date [m]
           (sort-by #(vec (map % "date")) m))
         (defn date-human-readable [m]
           (map (fn [i]
                  (update-in i ["date"]
                             (fn [a]
                               (.fromNow
                                (.tz js/moment a "YYYY-MM-DD hh:mm:ss" "Etc/UTC"))))) m))
         (defn add-down-up [v i]
           (let [up-down (if (= (first v) null) "" (if (>= (i "rate") ((first v) "rate")) "up" "down"))]
             (cons (assoc i "up-down" up-down) v)))
         (let [sort (reduce add-down-up [] (reverse (take 20 (sort-by-date (date-human-readable h)))))]
           (map HistoryItem sort))))

(q/defcomponent PriceInfo [d]
  (d/div {}
         (Ticker ((d "ticker") "last"))
         (History (d "history"))))


(def periodlist (list "6h" "24h" "2d" "4d" "1w" "2w" "1m" "all"))

(q/defcomponent ChartControl []
  (apply d/div {}  (map ChartControlItem periodlist)))

(q/defcomponent ChartControlItem [period] (d/a {:onClick #(GET (str "chart/" period "/") {:handler chart-ajax-handler})} (str period " ")))

(defn chart-ajax-handler [response]
  (.buildChart (.-XMR js/window) (.parse js/JSON (str response))))

(enable-console-print!)

(q/render (ChartControl) (.getElementById js/document "chart-control"))

(go
  (let [server-ch (<! (ws-ch "ws://jakoblind.se/xmr/ws"{:format :edn}))
        container (.getElementById js/document "main")]
    (go-loop []
      (when-let [d (:message (<! server-ch))]
        (set! (.-title js/document) (str ((d "ticker") "last") " BTC/XMR"))
        (q/render (PriceInfo d) container)
        (recur)))))

; TODO ajax stuff here(.buildChart (.-XMR js/window) )
