(defproject plomb "0.1.0-SNAPSHOT"
  :description "sugar macros for core.async chans between om components"
  :url "https://github.com/selfsame/plomb/"
  :license {:name "The MIT License (MIT)"
            :url "https://github.com/selfsame/plomb/blob/master/LICENSE"}

  :source-paths ["src"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173" :scope "provided"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha" :scope "provided"]
                 [om "0.6.4"]]

  :plugins [[lein-cljsbuild "1.0.1"]]


  :cljsbuild {:builds
              [{:id "simple"
                :source-paths ["src" "examples/simple/src"]
                :compiler {
                           :output-to "examples/simple/main.js"
                           :output-dir "examples/simple/out"
                           :source-map true
                           :optimizations :none}}]})



