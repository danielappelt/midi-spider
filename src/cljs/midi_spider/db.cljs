(ns midi-spider.db)

(def default-db
  {:active-panel :midi-spider.views/home
   :inputs []
   :input #js {}
   :in-buffer (js/Uint8Array.)
   :download-url nil
   :outputs []
   :output #js {}
   ;; Sysex device inquiry
   :out-buffer (map #(js/parseInt % 16) ["f0" "7e" "7f" "06" "01" "f7"])})
