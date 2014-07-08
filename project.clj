(defproject org.clojars.selfsame/plomb "0.1.0-SNAPSHOT"
  :description "sugar macros for core.async chans between om components"
  :url "http://github.com/selfsame/plomb"
  :license {:name "The MIT License (MIT)"
            :url "https://github.com/selfsame/plomb/blob/master/LICENSE"}

  :source-paths ["src"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha" :scope "provided"]
                 [om "0.6.4"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]


  :cljsbuild {:builds
              [{:id "release"
                :source-paths ["src"]
                :compiler
                {:output-to "core.js"
                 :output-dir "out"
                 :optimizations :none
                 :output-wrapper false}}
               {:id "simple"
                :source-paths ["src" "examples/simple/src"]
                :compiler {
                           :output-to "examples/simple/main.js"
                           :output-dir "examples/simple/out"
                           :source-map true
                           :optimizations :none}}]})



