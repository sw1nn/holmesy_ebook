(ns holmesy-ebooks.core
  (:require [twitter.api.restful :refer [statuses-update]]))


;; You need to create a twitter read/write twitter account and fill
;; these in:

;; (def my-creds (make-oauth-creds
;;                *app-consumer-key*
;;                *app-consumer-secret*
;;                *user-access-token*
;;                *user-access-token-secret*))

(def my-creds nil)

(defn tweet [t]
  (statuses-update :oauth-creds my-creds
                   :params {:status t}))

(defn split-into-sentences [book]
  (drop 1000 (re-seq #"[A-Z][A-Za-z, !;:'\?]{10,140}" book)))

(defn random-sentence [book]
  (let [sentences (split-into-sentences book)
        num (count sentences)]
    (fn [] (nth sentences (rand-int num) ))))

(defn main-twitter [& [tweet?]]
  (let [fn (random-sentence (slurp "resources/sherlock-holmes.txt"))]
    (while true
      (let [text (fn)]
        (when tweet? (tweet text))
        (println text))
      (Thread/sleep 1000))))

(comment

(main-twitter)

(main-twitter :tweet)

)
