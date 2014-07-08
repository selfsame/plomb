(ns examples.simple.core
  (:require
      [om.core :as om :include-macros true]
      [om.dom :as dom :include-macros true]
      [plomb.core :as plomb :include-macros true]))

;defcomp uses a map form to define om components

(plomb/defcomp talker
  [data owner opts]
  {:render-state
   (fn [_ state]
     (let [message (:say data)]
       (dom/button
        #js {:onClick

             ; emit! sends a map payload to components
             ; that handle the given keyword

             #(plomb/emit! owner :talk {:m message})} message)))})

(plomb/defcomp hearer
  [data owner opts]
  {:render-state
    (fn [_ state]
      (dom/div #js {:className "container"} (:val data)))

   ;event handlers are defined as follows

   :catch {:talk
           (fn [e] (om/transact! data [:val] #(str % (:m e))))
           :erase
           (fn [e] (om/transact! data [:val] #(identity "")))}})

(plomb/defcomp app1
  [data owner opts]
  {:init-state
    (fn [_] {:count 0})
   :render-state
    (fn [_ state]
      (dom/div #js {:className "example"}
         (dom/h3 nil (:title data))
         (dom/div #js {:className "container"}
           (dom/span nil (:count state))
           (dom/button
            #js {:onClick #(plomb/emit! owner :erase {})} "clear"))
         (dom/div #js {:className "container"}
           (plomb/make talker (:foo data) {})
           (plomb/make talker (:fiz data) {}))
         (plomb/make hearer (:bar data) {})))

   ;under the hood, the component subscribes to a publisher for the event keyword chan
   ;subscriptions are removed when the component unmounts

   :catch {:talk
           (fn [e]
             (om/update-state! owner :count inc))
           :erase
           (fn [e]
             (om/set-state! owner :count 0))}})

(def ex1 (atom {:title "global events with emit!"
                 :foo {:say "hello. "}
                 :fiz {:say "seeya. "}
                 :bar {:val ""}}))

(om/root app1 ex1 {:target (.getElementById js/document "ex1")})









(plomb/defcomp leaf
  [data owner opts]
  {:init-state
    (fn [_] {:s nil})
   :render-state
    (fn [_ state]
      (dom/div #js {:className "leaf"
                    :style #js {:border-color (or (:s state) "silver")}}
         (when (:u data)
           (dom/button #js {:onClick
                            (fn [e]
                              (plomb/emit! owner :clear {})
                              (plomb/up! owner :select {:c "orange"}))} "up!"))
         (when (:d data)
           (dom/button #js {:onClick
                            (fn [e]
                              (plomb/emit! owner :clear {})
                              (plomb/down! owner :select {:c "lightBlue"}))} "down!"))

         (when (:ch data)
           (apply dom/span nil
             (plomb/make-all leaf (:ch data) {})))))
   :catch {:clear
           (fn [e] (om/set-state! owner :s false))
           :select
           (fn [e] (om/set-state! owner :s (:c e)))}})

(plomb/defcomp app2
  [data owner opts]
  {:init-state
     (fn [_] {:count 0})
   :render-state
     (fn [_ state]
       (dom/div #js {:className "example"}
                (dom/h3 nil (:title data))
                (plomb/make leaf (:tree data) {})))})

(def ex2 (atom {:title "up! and down! the component heirarchy"
                :tree {:d 1
                       :ch [{:ch [{:u 1}]}
                            {:ch [{:u 1}
                                  {:ch [{:u 1}
                                        {:d 1
                                         :ch [{:u 1}
                                              {:ch [{:u 1}
                                                    {:u 1}]}]}]}]}
                            {:d 1 :ch [{:ch [{:u 1}]}]}]}}))

(om/root app2 ex2 {:target (.getElementById js/document "ex2")})

