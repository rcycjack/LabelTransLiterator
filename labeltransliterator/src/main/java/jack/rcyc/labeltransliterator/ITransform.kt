package jack.rcyc.labeltransliterator

interface ITransform {

    fun transliterate(label: String?): String?
}