(defproject midi-spider "0.2.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.764"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.9.3"]
                 [reagent "0.10.0"]
                 [re-frame "0.12.0"]
                 [bidi "2.1.6"]]

  :plugins [[lein-shadow "0.2.0"]
            
            [lein-shell "0.5.0"]]

  :min-lein-version "2.9.0"

  :jvm-opts ["-Xmx1G"]

  :source-paths ["src/clj" "src/cljs"]

  :test-paths   ["test/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]


  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}

  :shadow-cljs {:nrepl {:port 8777}
                
                :builds {:app {:target :browser
                               :output-dir "resources/public/js/compiled"
                               :asset-path "/js/compiled"
                               :modules {:app {:init-fn midi-spider.core/init
                                               :preloads [devtools.preload]}}

                               :devtools {:http-root "resources/public"
                                          :http-port 8280
                                          }}
                         :browser-test
                         {:target :browser-test
                          :ns-regexp "-test$"
                          :runner-ns shadow.test.browser
                          :test-dir "target/browser-test"
                          :devtools {:http-root "target/browser-test"
                                     :http-port 8290}}

                         :karma-test
                         {:target :karma
                          :ns-regexp "-test$"
                          :output-to "target/karma-test.js"}}}

  :aliases {"dev"          ["with-profile" "dev" "do"
                            ["shadow" "watch" "app"]]
            "prod"         ["with-profile" "prod" "do"
                            ["shadow" "release" "app"]]
            "build-report" ["with-profile" "prod" "do"
                            ["shadow" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                            ["shell" "open" "target/build-report.html"]]
            "karma"        ["with-profile" "prod" "do"
                            ["shadow" "compile" "karma-test"]
                            ["shell" "./karma" "start" "--single-run" "--reporters" "junit,dots"]]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "1.0.0"]]
    :source-paths ["dev"]}

   :prod {}
   
}

  :prep-tasks [])
