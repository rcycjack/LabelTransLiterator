package jack.rcyc.labeltransliterator

interface IConverter {
    fun convert(label: String): String?
}