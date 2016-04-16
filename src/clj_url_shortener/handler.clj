(ns clj-url-shortener.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn shorten [params]
  (str (:url params))
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
