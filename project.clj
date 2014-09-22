(defproject xmrmarkets "0.1.0-SNAPSHOT"
  :description "XMR market ticker"
  :url "http://jakoblind.se/xmr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [quiescent "0.1.1"]
                 [http-kit "2.1.16"]
                 [compojure "1.1.8"]
                 [ring/ring-devel "1.1.8"]
                 [ring/ring-core "1.1.8"]
                 [clj-time "0.8.0"]
                 [cljs-ajax "0.2.6"]
                 [hiccup "1.0.4"]
                 [prismatic/dommy "0.1.3"]
                 [org.clojure/tools.logging "0.3.0"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
                 [org.slf4j/slf4j-log4j12 "1.6.6"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/tools.cli "0.2.2"]
                 [jarohen/chord "0.3.1"]]
  :main xmrmarkets.web

  :source-paths ["src"]
  :plugins[[lein-cljsbuild "1.0.3"]]

  :profiles {:uberjar {:aot :all
                       :hooks [leiningen.cljsbuild]}}
  :cljsbuild {:builds[{
                       :id "main"
                       :source-paths ["src"]
                                        ;                                        :jar true
                       :compiler {
                                  :output-to "target/classes/public/app.js"
                                        ;:output-to "resources/public/app.js"
                                  :output-dir "target/classes/public"
                                  :optimizations :whitespace
                                  :pretty-print true
                                  }}]})
