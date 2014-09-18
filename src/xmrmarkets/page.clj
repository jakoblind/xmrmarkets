(ns xmrmarkets.page (:require
                     [clj-time.core :as t]
                     [clj-time.coerce :as c]
                     [xmrmarkets.poloniex :as poloniex]
                     [hiccup.page :refer [html5 include-js include-css]]))

(defn page-frame [price history]
  (html5
   [:head
    [:title (str  " BTC/XMR")]]
   [:body
    [:div.container
    [:div#menucontainer
           [:div#tickercontainer [:div.ticker [:div.ticker-price price] [:div.ticker-currency "BTC/XMR"]]]
           [:div#chart-control  {:class "menu"} (str "6h " "24h " "2d " "4d " "1w " "2w " "1m " "all ")]
           [:div#market-menu {:class "menu selected-menu"} "poloniex"]]
    [:div#pricecontainer
              [:div#main
               [:div.history-container
                (map (fn [item]
                       [:div.history-item
                        [:div.history-item-xmr (item "amount")]
                        [:div.history-item-time (item "date")]
                        [:div.history-item-price (item "rate")]
                        [:div.history-item-updown
                         [:i {:class (str "fa fa-caret-" (item "up-down") " " (item "up-down"))}]]])
                     history)]]]
    [:div#chartarea
     [:div#chartcontainer[:div#chart]]]]
    (include-css "font-awesome-4.2.0/css/font-awesome.min.css")
    (include-css "main.css")
    (include-js "react.js")
    (include-js "d3.min.js")
    (include-js "candlestick.js")
    (include-js "moment.js")
    (include-js "moment-timezone.js")
    (include-js "app.js")]))


(defn- time-minus [amount] (long (/ (c/to-long (t/minus (t/now) amount)) 1000)))

(defn- periodmap [] {
                     "6h" (list (time-minus (t/hours 6)) 9999999999 1800)
                     "24h" (list (time-minus (t/hours 24)) 9999999999 7200)
                     "2d" (list (time-minus (t/days 2)) 9999999999 7200)
                     "4d" (list (time-minus (t/days 4)) 9999999999 7200)
                     "1w" (list (time-minus (t/weeks 1)) 9999999999 14400)
                     "2w" (list (time-minus (t/weeks 2)) 9999999999 14400)
                     "1m" (list (time-minus (t/months 1)) 9999999999 14400)
                     "all" (list (time-minus (t/years 5)) 9999999999 86400)})

(defn chart [period]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    @(apply poloniex/get-xmr-chart-history ((periodmap) period))})
