(defproject lollipop "0.1.0-SNAPSHOT"
  :description "Something of an 'echo' server.  You give it something and it will return the same."
  :url "https://github.com/justone/lollipop"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [http-kit "2.1.18"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler lollipop.handler/app}
  :main lollipop.handler
  :aot [lollipop.handler]
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
