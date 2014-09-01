(ns xmrmarkets.web
  (:require [org.httpkit.server :refer [run-server]]
	    [org.httpkit.client :as http]
	    [compojure.core :refer [defroutes GET]]
	    [compojure.route :refer [resources]]
	    [chord.http-kit :refer [wrap-websocket-handler]]
	    [clojure.data.json :as json]
	    [clojure.edn :as edn]
	    [clojure.core.async :refer [<! >! put! close! go go-loop]]))

(defn get-xmr-ticker []
  "get xmr ticker and convert to edn"
  (http/get "https://poloniex.com/public?command=returnTicker"
	    (fn [{:keys [status headers body error]}]
	      (prn-str (json/read-str body) "BTC_XMR"))))

(def lolz {:a 1 :b 2})

(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})


(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (let [t (get-xmr-ticker)]
    (go
      (>! ws-channel @t))))

(defroutes app
  (resources "/")
  (GET "/" [] index)
  (GET "/ws" [] (-> ws-handler
		    (wrap-websocket-handler {:format :json-kw}))))

(defn -main [& args]
  (run-server app {:port 8080})
  (println "server started"))
