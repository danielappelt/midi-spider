(ns midi-spider.db)

(def default-db
  {:inputs []
   :input #js {}
   :in-buffer []
   :outputs []
   :output #js {}
   ;; Sysex device inquiry
   :out-buffer (map #(js/parseInt % 16) ["f0" "7e" "7f" "06" "01" "f7"])})
