package jack.rcyc.labeltransliterator

class LabelComparable(
    val label: String,
    private val charInsertionPositions: List<Int>?
) : Comparable<LabelComparable> {

    private val excludeETBString by lazy {
        extractLabel()
    }

    val etbPositionsInNameList by lazy {
        extractOffsetsIfNeeded()
    }


    private fun getAdjustName(): String {
        return this.excludeETBString
    }


    override fun compareTo(other: LabelComparable): Int {
        return getAdjustName().compareTo(other.getAdjustName())
    }


    private fun extractLabel(): String {

        // Define a StringBuilder with an initial capacity of up to 16 characters
        val stringBuilder = StringBuilder(minOf(label.length, 16))

        // Iterate through each character in the label
        for (char in label) {
            // Append the character if it is not the ASCII character with value 23
            if (char.code != 23) {
                stringBuilder.append(char)
            }
        }

        // Return the final filtered string
        return stringBuilder.toString()
    }


    private fun extractOffsetsIfNeeded(): List<Int> {
        // If object2 is null, we compute and initialize it
        if (charInsertionPositions == null) {
            val result = mutableListOf<Int>()
            var offset = 0

            var index = 0
            while (index < label.length) {
                val codePoint = label.codePointAt(index)

                // If the codePoint is 23, record its relative offset
                if (codePoint == 23) {
                    result.add(index - offset)
                    offset++
                }

                // Advance by the number of chars in the current codePoint (handles surrogate pairs)
                index += Character.charCount(codePoint)
            }

            return result
        }

        return charInsertionPositions
    }

}