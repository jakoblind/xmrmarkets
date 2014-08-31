(ns xmrmarkets.web
  (:require [org.httpkit.server :refer [run-server]])
  (:require [compojure.core :refer [defroutes GET]])
  (:require [compojure.route :refer [resources]]))

(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defroutes app
  (resources "/")
  (GET "/" [] index))

(defn -main [& args]
  (run-server app {:port 8080})
  (println "server started"))
