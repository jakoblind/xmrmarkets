(ns xmrmarkets.web
  (:require [org.httpkit.server :as h])
  (:require [compojure.core :refer [defroutes GET]])
  (:require [compojure.route :refer [resources]]))

(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn handler [request]
  (h/with-channel request channel
    (println channel " connected")
    (h/on-close channel (fn [status] (println "channel closed: " status)))
    (h/on-receive channel (fn [data] ;; echo it back
			  (h/send! channel data)))))

(defroutes app
  (resources "/")
  (GET "/" [] index)
  (GET "/w" [] handler))

(defn -main [& args]
  (h/run-server app {:port 8080})
  (println "server started"))
