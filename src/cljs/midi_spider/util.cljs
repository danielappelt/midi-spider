(ns midi-spider.util
  (:require
   [clojure.string :as str]
   ))

(defn port-by-id [ports id]
  (first (filter #(= (.-id %) id) ports)))

(defn int->hex
  "Convert number n into hexadecimal format. Please note that the result will only
   be correct for numbers in the range [0, 15]."
  [n]
  (str/join (take-last 2 (str "0" (.toString n 16)))))

(defn binaryBuffer->text
  "Convert binary array into a string of hex numbers separated by a space."
  [buffer]
  (str/join " " (map int->hex buffer)))

(defn hex->int [s]
  (js/parseInt s 16))

(defn strip-to-hex
  "Strip everything from s that does not adhere to [a-fA-f0-9 ]. Remove
   leading spaces afterwards."
  [s]
  (str/replace (str/replace s #"[^a-fA-f0-9 ]" "") #"^[ ]*" ""))

(defn text->hex-array
  "Remove non-hex characters from text, add a trailing space in order to avoid
   an array containing the empty string and then split everything at >0 spaces."
  [text]
  (str/split (str (strip-to-hex text) " ") #"[ ]+"))

(defn maplike->seq
  "Converts a maplike JavaScript object into a Clojure sequence. Use a real JavaScript
   array as intermediate step."
  [map]
  (let [arr #js []]
    (.forEach map #(.push arr %))
    (js->clj arr)))

(defn maplike-first
  "Returns the first entry in a maplike JavaScript object, or nilEntry if m is empty.
   nilEntry defaults to an empty JavaScript object. For a definition of maplike see
   https://heycam.github.io/webidl/#dfn-maplike"
  ([m]
   (maplike-first m #js {}))
  ([m nilEntry]
   (or (.-value (.next (.values m))) nilEntry)))

(defn concatenate-buffers
  [x y]
  (let [z (js/Uint8Array. (+ (.-length x) (.-length y)))]
    (.set z x 0)
    (.set z y (.-length x))
    z))

