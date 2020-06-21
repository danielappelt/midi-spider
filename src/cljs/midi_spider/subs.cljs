(ns midi-spider.subs
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]
   [midi-spider.util :as util]))

(re-frame/reg-sub
 ::active-panel
 (fn [db]
   (:active-panel db)))

(re-frame/reg-sub
 ::inputs
 (fn [db]
   (:inputs db)))

(re-frame/reg-sub
 ::in-buffer
 (fn [db]
   (:in-buffer db)))

(re-frame/reg-sub
 ::download-url
 (fn [db]
   (:download-url db)))

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
 util/binaryBuffer->text)

(re-frame/reg-sub
 ::in-buffer-text
 (fn []
   (re-frame/subscribe [::in-buffer]))
 (comp util/binaryBuffer->text util/maplike->seq))
