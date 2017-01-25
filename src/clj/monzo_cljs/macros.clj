(ns monzo-cljs.macros)

(defmacro def-mdl-components [names]
  `(do
     ~@(map (fn [name]
              `(def ~name (reagent.core/adapt-react-class (aget js/ReactMDL ~(str name)))))
            names)))
