(ns midi-spider.events
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]
   [re-frame.loggers :refer [console]]
   [midi-spider.db :as db]
   [midi-spider.util :as util]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-fx
 ::midi-access
 (fn [{:keys [db]} [_ midi]]
   ;; Listen to port registrations and update the UI accordingly
   (set! (.-onstatechange midi) #(re-frame/dispatch [::update-midi-ports (.-port %)]))
   {:db (assoc db :midi midi)
    :dispatch-n (list [::update-midi-ports]
                      [::init-port-selection midi])}))
 
(re-frame/reg-event-db
 ::update-midi-ports
 ;; Recalculate MIDI inputs and outputs
 (fn [db [_ port]]
   (let [midi (:midi db)]
     (assoc db
            :inputs (util/maplike->seq (.-inputs midi))
            :outputs (util/maplike->seq (.-outputs midi))))))

(re-frame/reg-event-fx
 ::init-port-selection
 (fn [cofx [_ midi]]
   {:dispatch-n (list [::change-input (util/maplike-first (.-inputs midi))]
                      [::change-output (util/maplike-first (.-outputs midi))])}))

(re-frame/reg-event-fx
 ::midi-error
 (fn [cofx [_ error]]
   ;; TODO: report errors to the frontend
   (console :warn error)
   {}))

;; TODO: Put events and subscriptions that belong together into a common namespace.
;; see https://purelyfunctional.tv/guide/database-structure-in-re-frame/#events-and-subscriptions
(re-frame/reg-event-fx
 ::select-input
 (fn [{:keys [db]} [_ id]]
   {:dispatch [::change-input (util/port-by-id (:inputs db) id)]}))

(re-frame/reg-event-db
 ::change-input
 (fn [db [_ port]]
   (set! (.-onmidimessage (:input db)) nil)
   (set! (.-onmidimessage port) #(re-frame/dispatch [::midi-message (.-data %)]))
   (assoc db :input port)))

(re-frame/reg-event-db
 ::midi-message
 (fn [db [_ data]]
   ;; Free memory (https://developer.mozilla.org/en-US/docs/Web/API/URL/createObjectURL#Memory_management)
   (.revokeObjectURL js/URL (:download-url db))
   ;; Keep all messages received since last send operation
   (let [all-data (util/concatenate-buffers (:in-buffer db) data)]
     (assoc db
            :in-buffer all-data
            ;; We store the download URL in db in order to be able to revoke it later.
            :download-url (.createObjectURL js/URL
                                            (js/Blob. #js [all-data]
                                                      #js {:type "application/octet-stream"}))))))

(re-frame/reg-event-fx
 ::select-output
 (fn [{:keys [db]} [_ id]]
   {:dispatch [::change-output (util/port-by-id (:outputs db) id)]}))

(re-frame/reg-event-db
 ::change-output
 (fn [db [_ port]]
   (assoc db :output port)))

(re-frame/reg-event-db
 ::change-out-buffer
 (fn [db [_ value]]
   (assoc db
          :out-buffer value
          :out-update-time (.now js/Date))))

(re-frame/reg-event-fx
 ::change-out-file
 (fn [cofx [_ file]]
   ;; Load file into out-buffer
   ;; For now, we only support binary files:
   ;; https://github.com/mido/mido/blob/1077ffeb412cfe7673e9cbc091ec093d1252d7f8/mido/syx.py#L9
   ;; See https://clojurescript.org/guides/promise-interop
   (.then (.arrayBuffer file)
          #(re-frame/dispatch [::change-out-buffer (util/maplike->seq (js/Uint8Array. %))]))
   {}))

(re-frame/reg-event-fx
 ::change-out-buffer-text
 (fn [cofx [_ value]]
   {:dispatch [::change-out-buffer (map util/hex->int (util/text->hex-array value))]}))

(re-frame/reg-event-fx
 ::send-buffer
 (fn [{:keys [db]} _]
   ;; Reset in-buffer before sending next MIDI message
   {:db (assoc db :in-buffer (js/Uint8Array.))
    ::send-midi [(:output db) (:out-buffer db)]}))

(re-frame/reg-fx
 ::send-midi
 (fn [[output data]]
   (.send output data)))
