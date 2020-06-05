(ns midi-spider.routes
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]
   [bidi.bidi :as bb]
   [bidi.router :as br]
   [midi-spider.events :as events]
   ))

;; https://github.com/juxt/bidi#multiple-routes
(def routes ["" {"/" :midi-spider.views/home
                 "/about" :midi-spider.views/about}])

(defn init []
  (br/start-router! routes
                    {:on-navigate #(re-frame/dispatch
                                    [::events/set-active-panel (:handler %)])
                     :default-location {:handler :midi-spider.views/home}}))

(defn path-for [handler]
  (str "#" (bb/path-for routes handler)))
