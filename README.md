echelom
===

Sugar macros for linking [Om](https://www.github.com/swannodette/om/) components with core.async chans.

```clj
(ns examples.simple.core
  (:require
      [om.core :as om :include-macros true]
      [om.dom :as dom :include-macros true]
      [echelom.core :as ec :include-macros true]))

(ec/defcomp app
  [data owner opts]
  {:init-state
   (fn [_] {:count 0})
   :render-state
    (fn [_ state]
      (dom/button #js {:onClick #(ec/emit! owner :foo {})} (:count state)))
   :catch {:foo
           (fn [e] (om/update-state! owner :count inc))}})

(om/root app (atom {}) {:target (.-body js/document)})
```
