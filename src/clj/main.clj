(ns echelom.core)



(defmacro make
  ([func cursor data]
  `(om/build ~func ~cursor
               (update-in ~data [:init-state :_events] merge
                          (~'om/get-state ~'__owner :_events)))))

(defmacro make-all
  [func cursor data]
  `(om/build-all ~func ~cursor
               (update-in ~data [:init-state :_events] merge
                          (~'om/get-state ~'__owner :_events))))

(defmacro defcomp [name-symbol args map-body ]
  (let [app (or (first args) nil)
        owner (or (first (rest args)) nil)
        opts (or (first (rest (rest args))) nil)
        init-state (:init-state map-body)
        will-mount (or (:will-mount map-body) '(fn [_] ))
        did-mount (or (:did-mount map-body) '(fn [this#] ))
        should-update (:should-update map-body)
        will-update (:will-update map-body)
        did-update (:did-update map-body)
        will-unmount (or (:will-unmount map-body) '(fn [this#] ))
        render  (:render map-body)
        render-state  (:render-state map-body)
        catch-events (or (:catch map-body) {})
       ]

  `(defn ~name-symbol ~args
     (let [~'__owner ~owner
           ~'__opts ~opts]
     (~'reify
        ~@(when
          init-state `(
           ~'om.core/IInitState
           (~'init-state  ~(first (rest init-state))
         ~@(rest  (rest init-state)))))

        ~@(when
          will-mount `(
          ~'om.core/IWillMount
           (~'will-mount  ~(first (rest will-mount))
           (~'om/set-state! ~owner :__handlers ~catch-events)
           (~'echelom.core/-register-broadcasts ~catch-events)
           ~@(rest (rest will-mount)))))

      ~@(when
          did-mount `(
          ~'om.core/IDidMount
           (~'did-mount   ~(first (rest did-mount ))
            (~'echelom.core/-event-setup ~app ~owner)
         ~@(rest  (rest did-mount )))))

      ~@(when
         should-update `(
          ~'om.core/IShouldUpdate
          (~'should-update   ~(first (rest should-update ))
         ~@(rest  (rest should-update )))))

      ~@(when
         will-update `(
         ~'om.core/IWillUpdate
         (~'will-update   ~(first (rest will-update ))
         ~@(rest  (rest will-update )))))

      ~@(when
         did-update `(
         ~'om.core/IDidUpdate
         (~'did-update   ~(first (rest did-update ))
         ~@(rest  (rest did-update )))))

       ~@(when
         will-unmount `(
        ~'om.core/IWillUnmount
         (~'will-unmount   ~(first (rest will-unmount ))
         (~'echelom.core/-unmount-events ~app ~owner)
         ~@(rest  (rest will-unmount )))))

       ~@(when
         render `(
        ~'om.core/IRender
         (~'render  ~(first (rest render))
         ~@(rest  (rest render)))))

      ~@(when
        render-state `(
        ~'om.core/IRenderState
         (~'render-state  ~(first (rest render-state))
         ~@(rest  (rest render-state)))))

      )))))

