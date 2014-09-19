(ns xmrmarkets.web
  (:require [xmrmarkets.page :as page]
            [xmrmarkets.poloniex :as poloniex]
            [xmrmarkets.config :refer [config]]
            [org.httpkit.server :refer [run-server]]
            [org.httpkit.client :as http]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [compojure.core :refer [defroutes GET context]]
            [compojure.route :refer [resources]]
            [chord.http-kit :refer [wrap-websocket-handler]]
            [clojure.tools.cli :refer [cli]]
            [clojure.tools.logging :as log]
            [ring.util.response :refer [response]]
            [clojure.core.async :refer [<! >! put! close! go go-loop timeout]])(:gen-class))

(defonce server (atom nil))

(defonce latest-xmr-ticker (atom (poloniex/get-xmr-all)))

(defn all-xmr-history []
  (reduce #(assoc %1 %2 (:timestamp (c/to-long (t/now)) :history @(poloniex/get-xmr-chart-history %2))) {}(keys (poloniex/periodmap))))

(defonce cache-xmr-history (atom (all-xmr-history)))

(defn update-ticker-loop [] (future
                         (while (not (= @server nil))
                           (reset! latest-xmr-ticker (poloniex/get-xmr-all))
                           (Thread/sleep (:ticker-loop-interval config)))))

(defn update-history-loop [] (future
                         (while (not (= @server nil))
                           (reset! cache-xmr-history (all-xmr-history))
                           (Thread/sleep (:ticker-loop-interval config)))))


(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (go-loop []
    (>! ws-channel @latest-xmr-ticker)
    (<! (timeout (:ticker-ws-loop-interval config)))
    (when (not (= @server nil)) (recur))))

(defroutes routes
  (resources "/a/")
  (context "/a" []
           (GET "/" []
                (response (page/page-frame
                           ((@latest-xmr-ticker "ticker") "last") (take 23 (@latest-xmr-ticker "history")))))
           (GET "/chart/:period/" [period]
                (page/json (:history (@cache-xmr-history period))))
           (GET "/ws" [] (-> ws-handler
                               (wrap-websocket-handler {:format :edn})))))

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
      (reset! server (run-server handler {:port 8080}))
      (update-ticker-loop)
      (update-history-loop)
      (log/info "server started"))))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn reset []
  (stop-server)
  (-main))
