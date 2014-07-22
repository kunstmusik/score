(ns score.util
  (:import [javax.swing JOptionPane JTabbedPane
            JDialog JScrollPane JTextArea SwingUtilities
            AbstractAction JPopupMenu]
           [java.awt.event MouseAdapter]))

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
  [msg title]
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
      (.addMouseListener t
        (proxy [MouseAdapter] []
           (mousePressed [e]
             (when (SwingUtilities/isRightMouseButton e)
               (.show p t (.getX e) (.getY e))))))  
      (reset! tabs-dialog {:dialog dlg :tabs t})))
  (let [dlg (:dialog @tabs-dialog)
        t (:tabs @tabs-dialog)]
    (.add t title (JScrollPane. (JTextArea. msg)))
    (.setSelectedIndex t (- (.getTabCount t) 1))
    (.setVisible dlg true)))
