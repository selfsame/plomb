(ns echelom.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]]
                   [echelom.core :refer [defcomp make make-all BROADCAST-CHANS]])
  (:require
      [om.core :as om :include-macros true]
      [om.dom :as dom :include-macros true]
      [cljs.core.async :as async :refer [>! <! put! chan pipe pub sub unsub close!]]))

(defn- start-match [s1 s2]
  (when (and (string? s1) (string? s2))
    (let [s1c (count s1)
          s2c (count s2)
          s2sub (if (>= s2c s1c) (subs s2 0 s1c) nil)]
      (when s2sub
        (== s1 s2sub)))))

(def PRIVATE (atom {}))
(def BROADCAST-CHANS (atom {}))
(def BROADCAST-PUBS (atom {}))
(def COMPONENT-CHANS (atom {}))

(defn p-sub->c [p]
  (let [c (chan)]
        (sub p true c) c))

(defn owner-key [owner]
  (let [path (or (.-_rootNodeID owner) "?")]
    (str path)))

(defn private! [owner korks f]
  (let [func (if (= (type #()) (type f)) f (fn [v] f))
        kcol (if (sequential? korks) korks [korks])
        okey (owner-key owner)]
    (swap! PRIVATE update-in (cons okey kcol) func )))

(defn private
  ([owner]
   (private owner []))
  ([owner korks]
  (let [kcol (if (sequential? korks) korks [korks])
        okey (owner-key owner)]
    (get-in @PRIVATE (cons okey kcol)))))



(defn -register-broadcasts [handler-map]
  (let [names (keys handler-map)]
    (dorun
     (map
      (fn [k]
        (when-not (k @BROADCAST-CHANS)
          (swap! BROADCAST-CHANS #(conj % {k (chan)})))
        (when-not (k @BROADCAST-PUBS)
          (swap! BROADCAST-PUBS #(conj % {k (pub (k @BROADCAST-CHANS) map?)})))
        ) names))))

(defn -event-setup [app owner]
  (let [state (om/get-state owner)
        o-k (owner-key owner)]
      (when-let [__handlers (:__handlers state)]
          (let [subz
           (into {} (for [k (keys __handlers)
           :let [f (k __handlers)
                 p (k @BROADCAST-PUBS)
                 c (p-sub->c p)]]
             (do
               (go (while true
                     (let [event (<! c)]
                       ;(prn k o-k (:u-k event) (:d-k event))
                       (cond
                         (:u-k event)
                         (when (start-match o-k (:u-k event)) (f event))
                         (:d-k event)
                         (when (start-match (:d-k event)  o-k) (f event))
                         :else
                         (f event)
                       ))))
               {k c})))]
            (when (> (count subz) 0)
              (om/set-state! owner :__subs subz))))))


(defn -unmount-events [app owner]
  (let [state (om/get-state owner)]
    (let [subz (or (:__subs state) [])]
      (dorun
        (map (fn [k-sub]
               (let [k (first k-sub)
                     c (last k-sub)
                     p (k @BROADCAST-PUBS)]
               (unsub p true c)
                 )) subz))
       (om/set-state! owner :__subs '()))))



(defn emit! [owner k e]
  (when-let [c (k @BROADCAST-CHANS)]
    (put! c e)))

(defn up! [owner k e]
  (let [o-k (owner-key owner)]
    (when (map? e)
      (when-let [c (k @BROADCAST-CHANS)]
        (put! c (conj e {:u-k o-k} ))))))

(defn down! [owner k e]
  (let [o-k (owner-key owner)]
    (when (map? e)
      (when-let [c (k @BROADCAST-CHANS)]
        (put! c (conj e {:d-k o-k} ))))))




