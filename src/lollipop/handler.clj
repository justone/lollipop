(ns lollipop.handler
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.core.async :as async :refer [go <!! put! chan <! >!]]
            [clojure.string :as str]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def brain-chan (chan))

(go (loop [brain {}]
      (let [{:keys [type resp k v]} (<! brain-chan)]
        (println (str "received " type " for " k " = " v))
        (case type
          :put (do (recur (assoc brain k v)))
          :get (do (>! resp (get brain k "default value")) (recur brain))))))

; put in some starter values
(put! brain-chan {:type :put :k "one" :v "one one one one"})
(put! brain-chan {:type :put :k "two" :v "twodytwody"})

(defn get-data
  [request]
  (let [server-name (:server-name request)
        subdom (first (str/split server-name #"\."))
        resp-chan (chan)
        _ (put! brain-chan {:type :get :k subdom :resp resp-chan})
        value (<!! resp-chan)]
    value))

(defroutes app-routes
  (GET "/" request (get-data request))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [port]
  (run-server app {:port (Integer. port)}))
