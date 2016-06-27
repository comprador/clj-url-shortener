(ns clj-url-shortener.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as ringutil]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:import 
    org.apache.commons.validator.routines.UrlValidator))

(def validator (UrlValidator. (into-array ["http" "https"]) (. UrlValidator ALLOW_LOCAL_URLS)))

(def urls (atom {}))

(def id-generator
  (atom 0))

(defn next-id []
  (swap! id-generator inc))

(defn store [token url]
  (swap! urls assoc token url)
  (spit "target/out.data" @urls))

(defn load-url-from-file
  []
  (try
    (let [loaded-urls (read-string (slurp "target/out.data"))]
      (reset! urls loaded-urls))
       (catch Exception e
         (println e))))

(defn shorten [params]
  (let [url (:url params)]
    (if (.isValid validator url)
      (do
        (store (str (next-id)) url)
        (println @urls)
        (str "valid url: " url))
      (str "invalid url: " url)
      )
    )
  )

(defn expand [token]
  (println "expanding " token)
  (ringutil/redirect (@urls token))

  )


(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/s" {params :params} (shorten params))
  (GET "/x/:code"  [code] (expand code))
  (route/not-found "Not Found"))

(def app
  (do
    (println "Starting...")
    (load-url-from-file)
    (wrap-defaults app-routes site-defaults)))
