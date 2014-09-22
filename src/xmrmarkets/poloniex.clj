(ns xmrmarkets.poloniex
  (:require [xmrmarkets.daterelative :refer [date-relative-string]]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.tools.logging :as log]))

(defn get-xmr-ticker []
  (log/info "called get xmr ticker webservice PROD")
  (let [url "https://poloniex.com/public?command=returnTicker"]
    (http/get url
              (fn [{:keys [status headers body error]}]
                (if error
                  (do (log/error (str "error calling ws " url " " error)) nil)
                  ((get (json/read-str body) "BTC_XMR") "last"))))))

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
  (let [url "https://poloniex.com/public?command=returnTradeHistory&currencyPair=BTC_XMR"]
    (http/get url
              (fn [{:keys [status headers body error]}]
                (if error
                  (do (log/error (str "error calling ws " url " " error)) {})
                  (->> (json/read-str body)
                       (sort-by-date)
                       (take 26)
                       (date-human-readable)
                       (reduce-xmr-size )
                       (reverse)
                       (reduce add-down-up [])))))))

(defn- time-minus [amount] (long (/ (c/to-long (t/minus (t/now) amount)) 1000)))

(defn periodmap [] {
                     "6h" (lazy-seq (list (time-minus (t/hours 6)) 9999999999 1800))
                     "24h" (lazy-seq (list (time-minus (t/hours 24)) 9999999999 7200))
                     "2d" (lazy-seq (list (time-minus (t/days 2)) 9999999999 7200))
                     "4d" (lazy-seq (list (time-minus (t/days 4)) 9999999999 7200))
                     "1w" (lazy-seq (list (time-minus (t/weeks 1)) 9999999999 14400))
                     "2w" (lazy-seq (list (time-minus (t/weeks 2)) 9999999999 14400))
                     "1m" (lazy-seq (list (time-minus (t/months 1)) 9999999999 14400))
                     "all" (lazy-seq (list (time-minus (t/years 5)) 9999999999 86400))})

(defn get-xmr-chart-history [period]
  (let [[start end period] ((periodmap) period)]
       (log/info "called get xmr ticker webservice PROD")
       (let [url "https://poloniex.com/public?command=returnChartData&currencyPair=BTC_XMR&start="]
         (http/get (str url start "&end=" end "&period=" period)
                   (fn [{:keys [status headers body error]}]
                     (if error (do (log/error (str "error calling ws " url " " error)) {}))
                     body)))))
