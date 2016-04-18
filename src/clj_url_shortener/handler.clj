(ns clj-url-shortener.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:import 
    org.apache.commons.validator.routines.UrlValidator))

(def validator (UrlValidator. (into-array ["http" "https"]) (. UrlValidator ALLOW_LOCAL_URLS)))

(defn shorten [params]
  (let [url (:url params)]
    (if (.isValid validator url)
      (str "valid url: " url)
      (str "invalid url: " url)
      )
    )
  )

(defn expand [code]
  (str "expanding " code)
  )


(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/s" {params :params} (shorten params))
  (GET "/x/:code"  [code] (expand code))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
