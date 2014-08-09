(defproject kunstmusik/score "0.2.0-SNAPSHOT"
  :description "A generic library for musical score generation"
  :url "http://github.com/kunstmusik/score"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]

  :profiles  { :dev  {
                      :global-vars  {*warn-on-reflection* true} } })
