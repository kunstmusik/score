(defproject kunstmusik/score "0.3.0-SNAPSHOT"
  :description "A generic library for musical score generation"
  :url "http://github.com/kunstmusik/score"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]

  :profiles  { :dev  {
                      :global-vars  {*warn-on-reflection* true} } 

              :profiling  {
                           :plugins  [[lein-nodisassemble "0.1.3"]] 
                           :dependencies  [[org.clojure/clojure "1.7.0"]] 
                           :global-vars  {*warn-on-reflection* true
                                          *unchecked-math* :warn-on-boxed }} 
              }
  :scm {:name "git"
        :url "https://github.com/kunstmusik/score.git" })
