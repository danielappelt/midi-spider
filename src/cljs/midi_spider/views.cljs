(ns midi-spider.views
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]
   [midi-spider.events :as events]
   [midi-spider.subs :as subs]
   ))

(defn midi-port [port]
  ;; ^{:key (.-id port)}
  [:option {:value (.-id port)} (.-name port)])

(defn midi-ports [label subscription event]
  (let [ports @(re-frame/subscribe [subscription])
        id (name subscription)]
    ;; Use list to return multiple top-level nodes (see https://purelyfunctional.tv/mini-guide/hiccup-tips/#multiple-tags)
    (list
     ^{:key (str id "-label")}
     [:label {:for id} label]
     ^{:key (str id "-select")}
     [:select {:id id
               :on-change #(re-frame/dispatch [event (.-value (.-currentTarget %))])}
      (for [port ports]
        ^{:key (.-id port)}
        [midi-port port])])))

(defn main-panel []
  [:div
   [:h1 "MIDI Spider"]
   [:div
    (midi-ports "Output:" ::subs/outputs ::events/select-output)
    ;; A file input to load sysex from file
    [:input {:type "file" :accept ".syx"
             :on-change #(re-frame/dispatch [::events/change-out-file
                                             (aget (.-files (.-target %)) 0)])}]
    ;; Provide a key in order to trigger re-rendering on out-buffer-text changes.
    ;; Please note that we need to make sure this key changes in order to trigger
    ;; a re-render.
    ^{:key @(re-frame/subscribe [::subs/out-update-time])}
    [:textarea {:defaultValue @(re-frame/subscribe [::subs/out-buffer-text])
                :on-blur #(re-frame/dispatch [::events/change-out-buffer-text
                                              (.-value (.-target %))])}]
    [:input {:type "submit"
             :on-click #(re-frame/dispatch [::events/send-buffer])}]]
   [:div
    (midi-ports "Input:" ::subs/inputs ::events/select-input)
    [:textarea {:value @(re-frame/subscribe [::subs/in-buffer-text])
                :readOnly true}]]])
