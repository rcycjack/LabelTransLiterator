package jack.rcyc.labeltransliterator

import java.text.Normalizer
import java.util.regex.Pattern

class FallbackConverter : IConverter {

    // Holds a list of TransliterationConfig instances
    private val configs: List<TransformConfig> = listOf(
        TransformConfig(0),
        TransformConfig(1)
    )

    // Companion object holding static members
    companion object {
        val pattern: Pattern = Pattern.compile("[\\p{InCombiningDiacriticalMarks}+\\s]")
    }

    override fun convert(label: String): String {
        if (label.isEmpty()) return ""

        var result = label

        for (config in configs) {
            val builder = StringBuilder(result.length)

            var index = 0
            while (index < result.length) {
                val codePoint = result.codePointAt(index)
                val charAsString = String(Character.toChars(codePoint))

                // Attempt to get the mapped value, fallback to original character
                val mapped = config.map[charAsString] ?: charAsString
                builder.append(mapped)

                index += Character.charCount(codePoint)
            }

            result = builder.toString()
        }

        // Normalize and remove combining marks and whitespace
        val normalized = Normalizer.normalize(result, Normalizer.Form.NFKD)
        return pattern.matcher(normalized).replaceAll("")
    }
}
