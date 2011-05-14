 (ns ibycus.reader
   (:use [techne.files :only [file->string dir->files]])
   (:use [ibycus.tokenizer :only [string->words]])
   (:use [ibycus.vocab :only [words->vocab]]))

(defn- file->words
  [file]
  (-> 
    (file->string file)
    (string->words)))

(defn- files->vocab
  [files]
  (->
    (map #(file->words %) files)
    (flatten)
    (words->vocab )))

(defn create-from-dir [dir] 
  (files->vocab (dir->files dir)))

