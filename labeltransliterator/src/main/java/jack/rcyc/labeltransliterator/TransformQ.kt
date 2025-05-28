package jack.rcyc.labeltransliterator

import android.annotation.SuppressLint
import android.icu.text.Transliterator

class TransformQ(private val transform: Transliterator) : ITransform {

    @SuppressLint("NewApi")
    override fun transliterate(label: String?): String? {
        return transform.transliterate(label)
    }
}