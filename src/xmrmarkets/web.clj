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
	    [clojure.core.async :refer [<! >! put! close! go go-loop timeout]]))

(defn get-xmr-ticker []
  (log/info "called get xmr ticker webservice PROD")
  (http/get "https://poloniex.com/public?command=returnTicker"
	    (fn [{:keys [status headers body error]}]
	      (get (json/read-str body) "BTC_XMR"))))

(defn get-xmr-ticker-test []
  (log/info "called get xmr ticker webservice TEST")
  {:last 0.0042, :lowestAsk 0.00420393, :highestBid 0.0042, :percentChange -0.07950451, :baseVolume 390.0300689, :quoteVolume 87916.16362306, :isFrozen 99})

(def latest-xmr-ticker (atom (get-xmr-ticker-test)))

(defn update-ticker [] (future
		     (while true
		       (reset! latest-xmr-ticker (get-xmr-ticker-test))
		       (Thread/sleep 5000))
		     ))

(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (let [t @latest-xmr-ticker]
    (log/info t)
    (go-loop []
      (<! (timeout 1000))
      (>! ws-channel t)
      (recur))))

(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defroutes routes
  (resources "/")
  (GET "/" [] index)
  (GET "/ws" [] (-> ws-handler
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
