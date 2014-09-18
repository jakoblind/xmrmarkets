(ns xmrmarkets.poloniex
  (:require [xmrmarkets.daterelative :refer [date-relative-string]]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [clojure.tools.logging :as log]))

(defn get-xmr-ticker []
  (log/info "called get xmr ticker webservice PROD")
  (http/get "https://poloniex.com/public?command=returnTicker"
            (fn [{:keys [status headers body error]}]
              (get (json/read-str body) "BTC_XMR"))))

(defn get-xmr-trade-history []
  (log/info "called get xmr ticker webservice PROD")
  (defn todouble [in] (read-string in))
  (defn add-down-up [v i]
    (let [up-down (if (= (first v) nil) "" (if (>= (todouble (i "rate")) (todouble ((first v) "rate"))) "up" "down"))]
      (cons (assoc i "up-down" up-down) v)))
  (defn sort-by-date [m]
    (sort-by #(vec (map % "date")) m))
  (defn date-human-readable [m]
     (map (fn [i]
            (update-in i ["date"]
                       date-relative-string)) m))
  (defn reduce-xmr-size [m]
    (map (fn [i]
           (update-in i ["amount"]
                      (fn [a] (.format (new java.text.DecimalFormat "#.####")  (read-string a))))) m))
  (http/get "https://poloniex.com/public?command=returnTradeHistory&currencyPair=BTC_XMR"
            (fn [{:keys [status headers body error]}]
              (->> (json/read-str body)
                   (sort-by-date)
                   (take 26)
                   (date-human-readable)
                   (reduce-xmr-size )
                   (reverse)
                   (reduce add-down-up [])))))

(defn get-xmr-chart-history [start, end, period]
  (log/info "called get xmr ticker webservice PROD")
  (http/get (str "https://poloniex.com/public?command=returnChartData&currencyPair=BTC_XMR&start=" start "&end=" end "&period=" period)
            (fn [{:keys [status headers body error]}]
              body)))

(defn get-xmr-all []  {"ticker" @(get-xmr-ticker) "history" @(get-xmr-trade-history)})
