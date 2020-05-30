(ns midi-spider.events
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]
   [re-frame.loggers :refer [console]]
   [midi-spider.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn- maplike->seq [map]
  (let [arr #js []]
    (.forEach map #(.push arr %))
    (js->clj arr)))

(re-frame/reg-event-fx
 ::midi-access
 (fn [{:keys [db]} [_ midi]]
   ;; Listen to port registrations and update the UI accordingly
   (set! (.-onstatechange midi) #(re-frame/dispatch [::update-midi-ports (.-port %)]))
   {:db (assoc db :midi midi)
    :dispatch-n (list [::update-midi-ports] [::init-port-selection midi])}))
 
(re-frame/reg-event-db
 ::update-midi-ports
 ;; Recalculate MIDI inputs and outputs
 (fn [db [_ port]]
   (let [midi (:midi db)]
     (assoc db
            :inputs (maplike->seq (.-inputs midi))
            :outputs (maplike->seq (.-outputs midi))))))

(re-frame/reg-event-fx
 ::init-port-selection
 (fn [cofx [_ midi]]
   {:dispatch-n (list [::change-input (.-value (.next (.values (.-inputs midi))))]
                      [::change-output (.-value (.next (.values (.-outputs midi))))])}))

(re-frame/reg-event-fx
 ::midi-error
 (fn [cofx [_ error]]
   ;; TODO: report errors to the frontend
   (console :warn error)
   {}))

;; TODO: Put events and subscriptions that belong together into a common namespace.
;; see https://purelyfunctional.tv/guide/database-structure-in-re-frame/#events-and-subscriptions
(defn port-by-id [ports id]
  (first (filter #(= (.-id %) id) ports)))

(re-frame/reg-event-fx
 ::select-input
 (fn [{:keys [db]} [_ id]]
   {:dispatch [::change-input (port-by-id (:inputs db) id)]}))

(re-frame/reg-event-db
 ::change-input
 (fn [db [_ port]]
   (set! (.-onmidimessage (:input db)) nil)
   (set! (.-onmidimessage port) #(re-frame/dispatch [::midi-message (.-data %)]))
   (assoc db :input port)))

(re-frame/reg-event-db
 ::midi-message
 (fn [db [_ data]]
   (assoc db :in-buffer (maplike->seq data))))

(re-frame/reg-event-fx
 ::select-output
 (fn [{:keys [db]} [_ id]]
   {:dispatch [::change-output (port-by-id (:outputs db) id)]}))

(re-frame/reg-event-db
 ::change-output
 (fn [db [_ port]]
   (assoc db :output port)))

(re-frame/reg-event-fx
 ::change-out-file
 (fn [cofx [_ file]]
   ;; Load file into out-buffer
   ;; For now, we only support binary files:
   ;; https://github.com/mido/mido/blob/1077ffeb412cfe7673e9cbc091ec093d1252d7f8/mido/syx.py#L9
   ;; See https://clojurescript.org/guides/promise-interop
   (.then (.arrayBuffer file)
          #(re-frame/dispatch [::change-out-buffer (maplike->seq (js/Uint8Array. %))]))
   {}))

(re-frame/reg-event-db
 ::change-out-buffer
 (fn [db [_ value]]
   (assoc db
          :out-buffer value
          :out-update-time (.now js/Date))))

(defn hex->int [s]
  (js/parseInt s 16))

(defn strip-to-hex [s]
  "Strip everything from s that does not adhere to [a-fA-f0-9 ]. Remove
   leading spaces afterwards."
  (str/replace (str/replace s #"[^a-fA-f0-9 ]" "") #"^[ ]*" ""))

(defn text->hex-array [text]
  "Remove non-hex characters from text, add a trailing space in order to avoid
   an array containing the empty string and then split everything at >0 spaces."
  (str/split (str (strip-to-hex text) " ") #"[ ]+"))

(re-frame/reg-event-fx
 ::change-out-buffer-text
 (fn [cofx [_ value]]
   {:dispatch [::change-out-buffer (map hex->int (text->hex-array value))]}))

;; This event handler does not change db, use effects instead.
(re-frame/reg-event-fx
 ::send-buffer
 (fn [{:keys [db]} _]
   {::send-midi [(:output db) (:out-buffer db)]}))

(re-frame/reg-fx
 ::send-midi
 (fn [[output data]]
   (.send output data)))
