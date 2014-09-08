(ns xmrmarkets.web
  (:require [org.httpkit.server :refer [run-server]]
	    [org.httpkit.client :as http]
	    [ring.middleware.reload :as reload]
	    [compojure.handler :refer [site]]
	    [compojure.core :refer [defroutes GET]]
	    [compojure.route :refer [resources]]
	    [chord.http-kit :refer [wrap-websocket-handler]]
	    [clojure.data.json :as json]
	    [clojure.tools.cli :refer [cli]]
	    [clojure.tools.logging :as log]
	    [ring.util.response :refer [response]]
	    [hiccup.page :refer [html5 include-js include-css]]
	    [clojure.core.async :refer [<! >! put! close! go go-loop timeout]])(:gen-class))

(defn get-xmr-ticker []
  (log/info "called get xmr ticker webservice PROD")
  (http/get "https://poloniex.com/public?command=returnTicker"
	    (fn [{:keys [status headers body error]}]
	      (get (json/read-str body) "BTC_XMR"))))

(defn get-xmr-trade-history []
  (log/info "called get xmr ticker webservice PROD")
  (http/get "https://poloniex.com/public?command=returnTradeHistory&currencyPair=BTC_XMR"
	    (fn [{:keys [status headers body error]}]
	      (json/read-str body))))

(defn get-xmr-all []  {"ticker" @(get-xmr-ticker) "history" @(get-xmr-trade-history)})

(println @(get-xmr-trade-history))

(defn get-xmr-all-test []
  (log/info "called get xmr all webservice TEST")
   {"last" 0.00410137, "lowestAsk" 0.00412999, "highestBid" 0.00410137, "percentChange" -0.01171807, "baseVolume" 251.06992921, "quoteVolume" 61852.85822063, "isFrozen" 0})

(def latest-xmr-ticker (atom (get-xmr-all)))

(defn update-ticker [] (future
			 (while true
			   (reset! latest-xmr-ticker (get-xmr-all))
			   (Thread/sleep 60000))
			 ))

(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (go-loop []
    (>! ws-channel @latest-xmr-ticker)
    (<! (timeout 1000))
    (recur)))

(defn page-frame []
  (html5
   [:head
    [:title (get (get @latest-xmr-ticker "ticker") "last")]
    (include-css "font-awesome-4.2.0/css/font-awesome.min.css")
    (include-css "main.css")
    (include-js "react.js")
    (include-js "moment.js")
    (include-js "moment-timezone.js")
    (include-js "app.js")]
   [:body [:div#main[:div.ticker
		     [:div.ticker-price
				  (get (get @latest-xmr-ticker "ticker") "last")]
		     [:div.ticker-currency "BTC/XMR"]]]]))

(defroutes routes
  (resources "/a/")
  (GET "/a/" [] (response (page-frame)))
  (GET "/a/ws" [] (-> ws-handler
		    (wrap-websocket-handler {:format :edn}))))

(defn -main [& args]
  (let [[options args banner]
	(cli args
	     ["-h" "--help" "Show Help" :default false :flag true]
	     ["-d" "--development" "Run server in development mode" :default false :flag true])]
    (defonce in-dev? (:development options))
    (when (:help options)
	  (println banner)
	  (System/exit 0))
    (let [handler (if in-dev? (reload/wrap-reload (site #'routes)) (site routes))]
      (run-server handler {:port 8080})
      (update-ticker)
      (log/info "server started"))))
