 (ns ibycus.reader
   (:use [techne.files :only [file->string dir->files]])
   (:use [ibycus.tokenizer :only [string->words]])
   (:use [ibycus.vocab :only [words->vocab]]))

(defn- file->words
  [file]
  (-> 
    (file->string file)
    (string->words)))

(defn files->vocab
  [files]
  (->
    (map #(file->words %) files)
    (flatten)
    (words->vocab )))


(def vocab-files (dir->files "vocab"))

(def v (files->vocab vocab-files))

