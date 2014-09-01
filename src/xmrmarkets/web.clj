(ns xmrmarkets.web
  (:require [org.httpkit.server :refer [run-server]]
	    [compojure.core :refer [defroutes GET]]
	    [compojure.route :refer [resources]]
	    [chord.http-kit :refer [wrap-websocket-handler]]
	    [clojure.core.async :refer [<! >! put! close! go-loop]]))

(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})


(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (go-loop []
    (when-let [{:keys [message error] :as msg} (<! ws-channel)]
      (prn "Message received:" msg)
      (>! ws-channel (if error
		       (format "Error: '%s'." (pr-str msg))
		       {:received (format "You passed: '%s' at %s." (pr-str message) (java.util.Date.))}))
      (recur))))

(defroutes app
  (resources "/")
  (GET "/" [] index)
  (GET "/ws" [] (-> ws-handler
		    (wrap-websocket-handler {:format :json-kw}))))

(defn -main [& args]
  (run-server app {:port 8080})
  (println "server started"))
