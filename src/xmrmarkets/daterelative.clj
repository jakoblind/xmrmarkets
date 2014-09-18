(ns xmrmarkets.daterelative (:require
                             [clj-time.core :as t]
                             [clj-time.coerce :as c]
                             [clj-time.format :as f]))

(defn date-relative-string [d]
  (let [diff-sec
        (long (/ (- (c/to-long (t/to-time-zone (t/now) (t/time-zone-for-offset 0))) (c/to-long (f/parse (f/formatter "yyyy-MM-dd HH:mm:ss") d))) 1000))]
    (cond (<= diff-sec 10) "a few sec ago"
          (< diff-sec 60) (str diff-sec " sec ago")
          (< diff-sec 120) "1 min ago"
          (<= diff-sec (* 60 60)) (str (long (/ diff-sec 60)) " min ago")
          (< diff-sec (* 60 60 2)) "1 h ago"
          (<= diff-sec (* 60 60 12)) (str (long (/ diff-sec (* 60 60))) " h ago")
          (<= diff-sec (* 60 60 24)) "yesterday"
          :else d)))
