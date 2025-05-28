package jack.rcyc.labeltransliterator

data class TransformConfig(val type: Int) {

    // A read-only map holding character mappings depending on `who`
    val map: Map<String, String> = if (type != 1) {
        // For all other values of `who`, return a full Cyrillic-to-Latin map
        mapOf(
            "а" to "a", "б" to "b", "в" to "v", "г" to "g", "ѓ" to "g`", "ґ" to "g`",
            "д" to "d", "е" to "e", "ё" to "yo", "є" to "ye", "ж" to "zh", "з" to "z",
            "ѕ" to "z`", "и" to "i", "й" to "j", "і" to "i,", "ї" to "yi", "к" to "k",
            "ќ" to "k`", "л" to "l", "љ" to "l`", "м" to "m", "н" to "n", "њ" to "n`",
            "о" to "о", "п" to "p", "р" to "r", "с" to "s", "т" to "t", "у" to "u",
            "ў" to "u`", "ф" to "f", "х" to "x", "ц" to "cz", "ч" to "ch", "џ" to "dh",
            "ш" to "sh", "щ" to "shh", "ъ" to "\"", "ы" to "y`", "ь" to "`",
            "э" to "e`", "ю" to "yu", "я" to "уа", "ѣ" to "уе", "ѳ" to "fh",
            "ѵ" to "yh", "ѫ" to "о`"
        )
    } else {
        // For `who == 1`, return a simplified mapping
        mapOf("ß" to "ss")
    }
}
