(ns midi-spider.subs
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::active-panel
 (fn [db]
   (:active-panel db)))

(defn int->hex [n]
  (str/join (take-last 2 (str "0" (.toString n 16)))))

(defn hex->int [s]
  (js/parseInt s 16))

(defn binaryBuffer->text
  [buffer]
  (str/join " " (map int->hex buffer)))

(re-frame/reg-sub
 ::inputs
 (fn [db]
   (:inputs db)))

(re-frame/reg-sub
 ::in-buffer
 (fn [db]
   (:in-buffer db)))

(re-frame/reg-sub
 ::outputs
 (fn [db]
   (:outputs db)))

(re-frame/reg-sub
 ::out-buffer
 (fn [db]
   (:out-buffer db)))

(re-frame/reg-sub
 ::out-update-time
 (fn [db]
   (:out-update-time db)))

(re-frame/reg-sub
 ::out-buffer-text
 (fn []
   (re-frame/subscribe [::out-buffer]))
 binaryBuffer->text)

(re-frame/reg-sub
 ::in-buffer-text
 (fn []
   (re-frame/subscribe [::in-buffer]))
 binaryBuffer->text)
