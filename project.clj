(defproject sug "0.1.0-SNAPSHOT"
  :description "sugar for om"
  :url "https://github.com/selfsame/sug/"
  :license {:name "The MIT License (MIT)"
            :url "https://github.com/selfsame/sug/blob/master/LICENSE"}

  :source-paths ["src"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173" :scope "provided"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha" :scope "provided"]
                 [domina "1.0.2-SNAPSHOT"]
                 [om "0.6.4"]
                 ]

  :plugins [[lein-cljsbuild "1.0.1"]]


  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :compiler {
                           :output-to "main.js"
                           :output-dir "out"
                           :optimizations :whitespace
                           :pretty-print true}}

               {:id "test"
                :source-paths ["src" "tests/src"]
                :compiler {
                           :output-to "tests/tests.simple.js"
                           :output-dir "tests/out"
                           :output-wrapper false
                           :optimizations :simple}}
               {:id "simple"
                :source-paths ["src" "examples/simple/src"]
                :compiler {
                           :output-to "examples/simple/main.js"
                           :output-dir "examples/simple/out"
                           :source-map true
                           :optimizations :none}}
               {:id "sortable"
                :source-paths ["src" "examples/sortable/src"]
                :compiler {
                           :output-to "examples/sortable/main.js"
                           :output-dir "examples/sortable/out"
                           :optimizations :none}}
               {:id "sortable-production"
                :source-paths ["src" "examples/sortable/src"]
                :compiler {
                           :output-to "examples/sortable/main.js"
                           :output-wrapper false
                           :pretty-print false
                           :externs ["examples/sortable/react/react-0.8.0.min.js"]

                           :optimizations :advanced}}
               {:id "events"
                :source-paths ["src" "examples/events/src"]
                :compiler {
                           :output-to "examples/events/main.js"
                           :output-dir "examples/events/out"
                           :source-map true
                           :optimizations :none}}
               {:id "complex"
                :source-paths ["src" "examples/complex/src"]
                :compiler {
                           :output-to "examples/complex/main.js"
                           :output-dir "examples/complex/out"

                           :optimizations :none}}
               {:id "squares"
                :source-paths ["src" "examples/squares/src"]
                :compiler {
                           :output-to "examples/squares/main.js"
                           :output-dir "examples/squares/out"
                           :source-map true
                           :optimizations :none}}
               ]})



