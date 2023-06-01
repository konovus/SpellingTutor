package com.example.spellingnotify.data.local

object WordsData {

    private val three_letter_words = listOf(
        "add", "all", "app", "ass", "bee", "boo",
        "coo", "ebb", "eel", "egg", "ell", "err",
        "fee", "foo", "goo", "hee", "hmm", "ill",
        "inn", "lee", "nee", "odd", "off", "oof",
        "roo", "see", "tee", "too", "wee", "zoo",
    )

    private val four_letter_words = listOf(
        "ball", "been", "beep", "beer", "beet",

        "bell", "book", "boom", "boot", "bull", "butt", "call", "cell", "cook", "cool", "coon",

        "dekk", "doll", "doom", "door", "fall", "feed", "feel", "fell", "feet", "food", "fool",

        "foot", "full", "fuss", "gall", "gull", "hall", "heed", "heel", "hell", "hill", "hood",

        "hoof", "hoop", "hoot", "hull", "jeep", "keel", "keen", "keep", "knee", "less", "mall",

        "mood", "need", "pall", "pass", "peed", "peek", "peel", "pill", "pizza", "poll", "poof",

        "pool", "poor", "reed", "reef", "reel", "roll", "rood", "room", "root", "sass", "seed",

        "seek", "seem", "seen", "seep", "seer", "sell", "sill", "soon", "soot", "tall", "teed",

        "teem", "teen", "tell", "toll", "tool", "wall", "watt", "weed", "week", "weep", "well",
    )

    private val five_letter_words = listOf(
        "abyss",   "achoo", "added",

        "addle", "agree", "allay", "amass", "annex", "array", "atoll", "baggy", "balls",

        "betta", "bless", "bliss", "bluff", "bossy", "bunny", "chess", "chill", "class",

        "cliff", "comma", "crass", "daily", "dizzy", "dolly", "dress", "drill", "dwell",

        "fizzy", "floor", "floss", "fluff", "fully", "funny", "gabby", "gloss", "grass",

        "grill", "gross", "gruff", "happy", "heeled", "henna", "hurry", "igloo", "inner",

        "kazoo", "knoll", "latte", "lolly", "lotto", "melee", "messy", "missy", "offer",

        "patty", "penne", "piggy", "posse", "press", "puree", "quell", "quill", "rummy",

        "runny", "rupee", "scoff", "scuff", "shall", "silly", "sissy", "skiff", "skill",

        "skull", "small", "smell", "spell", "spoon", "spree", "staff", "stall", "steel",

        "stiff", "still", "stuff", "sunny", "swell", "tabby", "taboo", "taffy", "three",

        "tools", "trees", "trill", "troll", "tummy", "weeks", "wheel", "whoop", "yahoo",
    )

    private val six_letter_words = listOf(
        "accept", "across", "addle", "affair", "affect",

        "annual", "appeal", "appear", "arrive", "asleep", "assess", "assume", "attack",

        "baboon", "bamboo", "battle", "beeped", "beetle", "better", "boogie", "breeze",

        "bubble", "button", "called", "cannot", "cheese", "choose", "cocoon", "coffee",

        "common", "cookie", "cooler", "cotton", "creepy", "degree", "doodle", "esteem",

        "feeble", "feeder", "ferry", "follow", "fooled", "frilly", "gallop", "geezer",

        "gloomy", "goggle", "goober", "grabby", "granny", "groove", "heeded", "heeler",

        "hookah", "indeed", "juggle", "keeper", "killed", "lagoon", "latter", "letter",

        "litter", "loofah", "manner", "mutter", "needle", "noodle", "pepped", "rabbit",

        "really", "school", "screen", "seeded", "seemed", "shrill", "smooth", "speech",

        "spooky", "stalls", "street", "summer", "teepee", "tennis", "toffee", "unless",

        "vacuum", "valley", "weeded", "weekly", "weenie", "wheels", "yellow",
    )

    private val seven_and_more_letter_words = listOf(
        "account", "accused", "address", "alleged", "appealing",

        "applied", "arranged", "arrival", "assault", "assumed", "assumption", "assured",

        "attempt", "attract", "baggies", "beekeeper", "between", "billion", "blossom",

        "bookkeeper", "bubbles", "bunnies", "business", "calling", "channel", "classic",

        "collection", "command", "comment", "commentator", "communicate", "communication",

        "current", "disagree", "discuss", "dressage", "excommunicate", "express", "feelings",

        "filling", "fitness", "follower", "freedom", "funnies", "glitter", "goggled", "goggles",

        "grasshopper", "greetings", "illness", "installation", "killing", "littler", "message",

        "mission", "missionary", "moonlight", "noodles", "passage", "passenger", "passing",

        "proceed", "processing", "procession", "rubbish", "session", "shelled", "shuffle",

        "shuffled", "shuffles", "spaghetti", "starring", "steeled", "supposed", "tresses",

        "tummies", "village", "warring", "weekend", "wheedle", "willing", "witness", "written", "tell"
    )

    private val two_sets_of_double_letters_words = listOf(
        "aggression", "aggressive", "balloon",

        "bassoon", "bitterness", "bittersweet", "buffoon", "buttress", "cappuccino", "coffee",

        "commission", "commissioner", "coolly", "decommission", "embarrass", "foodstuff",

        "foolishness", "foot  ball", "fricassee", "fuzzball", "happiness", "lessee", "mattress",

        "millennium", "misspell", "misspelled", "misspelling", "mollycoddle", "riffraff",

        "sappiness", "settee", "sloppiness", "suffragette", "taffeta", "teepee", "toffee",
    )

    private val three_sets_of_double_letters_words = listOf(
        "addressee", "aggressiveness", "barrenness",

        "bookkeeper", "bookkeeping", "cheerlessness", "committee", "greenness",

        "heedlessness", "keenness", "possessiveness", "sleeplessness", "stubbornness",

        "subcommittee", "suddenness", "sullenness", "unsuccessfully", "whippoorwill",
    )




    val allWords = listOf(three_letter_words + four_letter_words + five_letter_words
    + six_letter_words + seven_and_more_letter_words + two_sets_of_double_letters_words
    + three_sets_of_double_letters_words).flatten()

}
