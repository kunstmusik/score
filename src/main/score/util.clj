(ns score.util
  (:import [javax.swing JOptionPane JTabbedPane
            JDialog JScrollPane JTextArea SwingUtilities
            AbstractAction JPopupMenu]
           [java.awt.event MouseAdapter MouseEvent]
           [java.io StringWriter])
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :refer [trimr]]))

(defn swapv! 
  "Swap a value in a vector using the given function."
  [v index func]
  (assoc v index (func (nth v index))))

(defn seq->gen 
  "Converts a sequence into a generator function with time arg"
  [vs]
  (let [curval (atom vs)] 
    (fn [t]
      (let [[a & b] @curval]
        (swap! curval rest) 
        a
        ))))

(defn debug-print
  "Decorates a generator function. Prints the generated value and passes through the value."
  [f]
  (fn [t]
    (let [v (f t)]
      (println ">>> " v) 
      v
      )))

(defn alert 
  "Show alert message in a JOptionPane dialog"
  ([msg]
   (alert msg "Message")
   )
  ([msg title]
  (JOptionPane/showMessageDialog nil msg title JOptionPane/PLAIN_MESSAGE)))

(def tabs-dialog (atom nil))

(defn show-info-tabs 
  "Show message in a global information dialog with tabs"
  [^String msg ^String title]
  (when (nil? @tabs-dialog)
    (let [dlg (JDialog.)
          t (JTabbedPane.)
          p (JPopupMenu.) ]
      (-> dlg (.getContentPane) (.add t)) 
      (.setTitle dlg "Information")
      (.setSize dlg 640 480)
      (.add p (proxy [AbstractAction] ["Close Tab"]
                (actionPerformed [e]
                  (let [index (.getSelectedIndex t)]
                    (when (>= index 0)
                      (.remove t index) 
                      (when (zero? (.getTabCount t))
                        (.setVisible dlg false)))))))
      (.add p (proxy [AbstractAction] ["Close All Tabs"]
                (actionPerformed [e]
                  (.removeAll t)
                  (.setVisible dlg false))))
      (.addMouseListener t
        (proxy [MouseAdapter] []
           (mousePressed [^MouseEvent e]
             (when (SwingUtilities/isRightMouseButton e)
               (.show p t (.getX e) (.getY e))))))  
      (reset! tabs-dialog {:dialog dlg :tabs t})))
  (let [dlg ^JDialog (:dialog @tabs-dialog)
        t ^JTabbedPane (:tabs @tabs-dialog)]
    (.add t title (JScrollPane. (JTextArea. msg)))
    (.setSelectedIndex t (- (.getTabCount t) 1))
    (.setVisible dlg true)))

(defn pprint-str
  "Use clojure's pprint but capture and return value as String"
  [x]
  (let [w (StringWriter.)]
    (pprint x w)
    (trimr (.toString w))))
