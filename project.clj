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
		 [org.clojure/data.json "0.2.5"]
		 [jarohen/chord "0.3.1"]]
  :main xmrmarkets.web
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
		   :plugins[[lein-cljsbuild "1.0.3"]]
		   :cljsbuild {:builds[{
					 :id "main"
					 :source-paths ["src"]
					:compiler {
						    :output-to "target/classes/public/app.js"
						    :output-dir "target/classes/public"
						    :optimizations :whitespace
						    :pretty-print true
						    }}]}}})
