(ns midi-spider.core
  (:require
   [reagent.core :as reagent]
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [midi-spider.events :as events]
   [midi-spider.views :as views]
   [midi-spider.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn request-midi-access [with-sysex]
  (-> js/navigator
      (.requestMIDIAccess #js {"sysex" with-sysex})
      (.then #(re-frame/dispatch [::events/midi-access %])
             #(re-frame/dispatch [::events/midi-error %]))))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (request-midi-access true)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
