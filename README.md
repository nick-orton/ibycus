# Ἴβυκος

Ibycus is a tool for generating ancient epic poetry via a Markov Chain

## Usage

    ibycus.main=> (def ibycus (build-ibycus)) 
    #'ibycus.main/ibycus

    ibycus.main=> (write-sentence ibycus)
    "Of th' eluded foe in the broad buckler half the weapon stood fix'd was the
     point but broken was the wood."

    ibycus.main=> (write-poem ibycus 100)
    "Erebus because of his beauty to be amongst the deathless gods you and your
     slaves alike in wet and in dry to plough in season and so the sailors fled
     into the stern and crowded bemused about the right-minded helmsman until
     suddenly the lion sprang upon the master and seized him and when the
     shipmen see them they are glad and have rest from their pain and labour.
     hail goddess! keep this city safe and govern my song. as when a flame the
     winding valley fills and runs on crackling shrubs between the hills then
     o'er the trench the"

## Installation

###leiningen

    [ibycus "0.0.0"]

## License

Copyright (C) 2011 Nick Orton

Distributed under the Eclipse Public License, the same as Clojure.
