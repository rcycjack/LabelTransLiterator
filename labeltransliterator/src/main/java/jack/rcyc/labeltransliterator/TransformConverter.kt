package jack.rcyc.labeltransliterator

/**
 * A Kotlin implementation of IConvert that uses a transform to convert strings.
 */
class TransformConverter(
    private var transform: ITransform? = null
) : IConverter {

    /**
     * Converts the input string using the provided transform.
     * If the transform is not set, returns null.
     *
     * @param label The string to be converted.
     * @return The transliterated string, or null if no transform is set.
     */
    override fun convert(label: String): String? {
        return transform?.transliterate(label)
    }
}