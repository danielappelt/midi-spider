(ns midi-spider.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [midi-spider.config :as config]
   [midi-spider.events :as events]
   [midi-spider.routes :as routes]
   [midi-spider.views :as views]
   ))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn request-midi-access
  "Request the browser for access to MIDI devices. This should in turn prompt the
   user for permission. An error event will be thrown if the browser lacks support
   for Web MIDI or if something else went wrong."
  [with-sysex]
  (if (nil? (.-requestMIDIAccess js/navigator))
    (#(re-frame/dispatch [::events/midi-error
                          "This browser does not support Web MIDI (see https://caniuse.com/#feat=midi)."]))
    (-> js/navigator
        (.requestMIDIAccess #js {:sysex with-sysex})
        (.then #(re-frame/dispatch [::events/midi-access %])
               #(re-frame/dispatch [::events/midi-error %])))))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (request-midi-access true)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (routes/init)
  (mount-root))
