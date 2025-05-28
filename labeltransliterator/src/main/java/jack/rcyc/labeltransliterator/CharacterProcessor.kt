package jack.rcyc.labeltransliterator


class CharacterProcessor(
    private var lastByte: Byte,
    private var currentByte: Byte,
    private val resultList: MutableList<Int>
) : Function2<Int, Int, Unit> {

    override fun invoke(startIndex: Int, codePoint: Int) {
        var v = 1

        // Step 1: Assign lastByte from currentByte
        lastByte = currentByte

        // Step 2: Determine the Unicode character type for the current code point
        val charType: Byte = Character.getType(codePoint).toByte()

        // Step 3: Update currentByte to hold the character type
        currentByte = charType

        // Step 4: Extract the last character's type (stored in lastByte)
        val lastCharType = lastByte.toInt()

        // Step 5: Run character-type comparison logic
        v = when {
            // If lastCharType is CONTROL, LINE_SEPARATOR, PARAGRAPH_SEPARATOR, or SPACE_SEPARATOR
            lastCharType in listOf(0, 12, 13, 14) -> {
                val separatorTypes = byteArrayOf(12, 13, 14)
                if(!separatorTypes.contains(charType)) 1 else 0
            }

            // If current is UPPERCASE or TITLECASE, and previous is UPPERCASE
            charType.toInt() in listOf(1, 3) && lastCharType == 1 -> 0

            // If current is not LOWERCASE
            charType.toInt() != 2 -> {
                when (charType.toInt()) {
                    in listOf(9, 10, 11) -> if (lastCharType in 9..11) 0 else -1
                    !in listOf(20, 24, 25, 26) -> 0
                    else -> -1
                }
            }

            // If current is LOWERCASE and previous is an identifier start
            charType.toInt() == 2 && lastCharType in 1..5 -> 0

            else -> -1
        }

        // Step 6: If valid transition found, record the start index
        if (v != 0) {
            resultList.add(startIndex)
        }


    }
}