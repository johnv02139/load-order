
(ns load-order.core
  (:require [clojure.tools.namespace.track :as track]
            [clojure.tools.namespace.dir :as dir])
  (:import (java.io File)
           (java.nio.file Path Paths Files)))

;; generic utilities

(def ^:private pwd (System/getProperty "user.dir"))

(defn- get-path
  "Creates a Path using the segments provided."
  [x & more]
  (let [more-array (into-array String more)]
    (Paths/get x more-array)))


;; data

(def ^:private cwd
  "The JVM's user dir, as a Path object."
  (get-path pwd))

(def ns-to-path
  "A mapping from namespace to Path object."
  (reduce
   (fn [m [file ns]]
     (assoc m ns (.toPath file)))
   {}
   (:clojure.tools.namespace.file/filemap
    (dir/scan-dirs (track/tracker)))))

;; functions

(defn ns-to-relative-path-string
  "The name of the relative path from which the given namespace was loaded."
  [ns root-path]
  (.toString
   (.relativize
    root-path
    (ns-to-path ns))))

(defn loaded-namespaces
  "The namespaces that are present, in the order they were loaded."
  []
  (-> (track/tracker)
      dir/scan-dirs
      ::track/load))

(defn print-require-all
  "Generate lines to require all namespaces, in order they were loaded."
  []
   (doseq [ns (loaded-namespaces)]
     (printf "            [%s :refer :all]%n" ns)))

(defn loaded-files
  "The clojure files in the order they have been loaded."
  []
  (map
   #(ns-to-relative-path-string % cwd)
   (loaded-namespaces)))

(defn print-loaded-files
  "See namespaces, one per line, in order they were loaded."
  ([fmt]
   (doseq [filepath (loaded-files)]
     (printf fmt filepath)))
  ([]
   (print-loaded-files " %s%n")))
