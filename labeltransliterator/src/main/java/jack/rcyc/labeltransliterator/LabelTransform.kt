package jack.rcyc.labeltransliterator

import android.annotation.SuppressLint
import android.icu.text.Transliterator
import android.icu.util.ULocale
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.annotation.RequiresApi
import java.lang.reflect.Method
import java.util.Collections
import java.util.Enumeration
import java.util.Locale
import java.util.regex.Pattern


class LabelTransform {


    companion object {

        private val pattern: Pattern = Pattern.compile("\\s+")
        fun deleteAllWhitespace(input: String): String {
            return pattern.matcher(input).replaceAll("")
        }

        fun isLowerCaseAscii(c: Char): Boolean {
            return c in 'a'..'z'
        }

        /**
         * Returns the script of the given [locale]. If it's not available,
         * attempts to infer it using ICU's likely subtags.
         *
         * @param locale The [Locale] to analyze.
         * @return The script code (e.g., "Latn", "Cyrl", "Arab") or an inferred one if not explicitly set.
         */
        @RequiresApi(Build.VERSION_CODES.N)
        fun getScriptOrLikely(locale: Locale): String {
            val script = locale.script
            return script.ifEmpty {
                // Use ICU to add likely subtags and extract the most probable script
                ULocale.addLikelySubtags(ULocale.forLocale(locale)).script
            }
        }
    }

    private var localeAlphabetMap: MutableMap<Char, LocaleAlphabetEnum> = mutableMapOf()
    private var locales: MutableList<Locale> = mutableListOf()

    private val converter: IConverter by lazy {
        initializeTransform()
    }

    init {
        initLocaleAlphabetMap()
    }


    private fun initializeTransform(): IConverter {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val localeList = LocaleList.getDefault()
            val scriptList = mutableListOf<String>()
            val locales = convertLocaleListToArrayList(localeList)
            val defaultLocale = Locale.getDefault()
            requireNotNull(defaultLocale) { "Default locale was null" }

            if ("ja" == defaultLocale.language) {
                scriptList.add("Hiragana-Latin")
                scriptList.add("Katakana-Latin")
            } else {
                for (locale in locales) {
                    if ("ja" == locale.language) {
                        scriptList.add("Hiragana-Latin")
                        scriptList.add("Katakana-Latin")
                        break
                    }
                }
            }

            if ("zh" == defaultLocale.language) {
                scriptList.add("Han-Latin")
            } else {
                for (locale in locales) {
                    if ("zh" == locale.language) {
                        scriptList.add("Han-Latin")
                        break
                    }
                }
            }


            if ("zh" == defaultLocale.language) {
                scriptList.add("Han-Latin")
            } else {
                for (locale in locales) {
                    if ("zh" == locale.language) {
                        scriptList.add("Han-Latin")
                        break
                    }
                }
            }

            if ("Arab" == getScriptOrLikely(defaultLocale)) {
                scriptList.add("Arabic-Latin")
            } else {
                for (locale in locales) {
                    if ("Arab" == getScriptOrLikely(locale)) {
                        scriptList.add("Arabic-Latin")
                        break
                    }
                }
            }

            if ("Cyrl" == getScriptOrLikely(defaultLocale)) {
                scriptList.add("Cyrillic-Latin")
            } else {
                for (locale in locales) {
                    if ("Cyrl" == getScriptOrLikely(locale)) {
                        scriptList.add("Cyrillic-Latin")
                        break
                    }
                }
            }

            if ("ru" == defaultLocale.language) {
                addTransliterateIfAvailable("Russian-Latin/BGN", scriptList)
            } else {
                for (locale in locales) {
                    if ("ru" == locale.language) {
                        addTransliterateIfAvailable("Russian-Latin/BGN", scriptList)
                        break
                    }
                }
            }

            if ("ko" == defaultLocale.language) {
                scriptList.add("Hangul-Latin")
            } else {
                for (locale in locales) {
                    if ("ko" == locale.language) {
                        scriptList.add("Hangul-Latin")
                        break
                    }
                }
            }

            if ("Grek" == getScriptOrLikely(defaultLocale)) {
                scriptList.add("Greek-Latin")
            } else {
                for (locale in locales) {
                    if ("Grek" == getScriptOrLikely(locale)) {
                        scriptList.add("Greek-Latin")
                        break
                    }
                }
            }


            if ("Hebr" == getScriptOrLikely(defaultLocale)) {
                scriptList.add("Hebrew-Latin")
            } else {
                for (locale in locales) {
                    if ("Hebr" == getScriptOrLikely(locale)) {
                        scriptList.add("Hebrew-Latin")
                        break
                    }
                }
            }
            scriptList.add("Latin-Ascii")
            scriptList.add("Any-Lower")

            val request = scriptList.joinToString(";")

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val transliterate = Transliterator.getInstance(request)
                    return TransformConverter(TransformQ(transliterate))
                } else {
                    val transliterateClass = Class.forName("android.icu.text.Transliterator")
                    val methodGetInstance: Method =
                        transliterateClass.getMethod("getInstance", String::class.java)
                    val methodTransliterate: Method =
                        transliterateClass.getMethod("transliterate", String::class.java)
                    val transliterateInstance = methodGetInstance.invoke(null, request)
                    if (transliterateInstance != null) {
                        return TransformConverter(object : ITransform {
                            override fun transliterate(label: String?): String? {
                                return try {
                                    methodTransliterate.invoke(
                                        transliterateInstance,
                                        label
                                    ) as String?
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        })
                    }
                }
            } catch (e: Exception) {
                Log.e("trans", e.toString())
            }
            return TransformConverter(null)
        } else {
            return FallbackConverter()
        }
    }


    fun getRepresentLetter(input: CharSequence?): String {
        if (input.isNullOrBlank()) return "#"

        var trimmed = input
        if (input.length >= 5) {
            trimmed = input.take(5)
        }

        val firstChar = Character.toLowerCase(trimmed.first())
        val localeAlphabetEnum =
            localeAlphabetMap[Character.valueOf(firstChar)] ?: LocaleAlphabetEnum.WITH_LETTER

        if (localeAlphabetEnum == LocaleAlphabetEnum.DISTINCT) {
            return firstChar.toString().uppercase(Locale.ROOT)
        } else if (localeAlphabetEnum == LocaleAlphabetEnum.SPECIALS_SECTION) {
            return "#"
        }


        val fallback = transform(trimmed, false)
        return if (!fallback.isNullOrBlank()) {
            val ch = fallback.first()
            if (isLowerCaseAscii(ch)) {
                ch.uppercaseChar().toString()
            } else {
                "#"
            }
        } else {
            "#"
        }
    }

    private fun transform(input: CharSequence?, flag: Boolean): String? {
        // Return null immediately if input is null
        if (input == null) return null

        // Get label based on flag
        val rawString = if (flag) {
            val labelComparable = getLabelComparable(input.toString())
            labelComparable.label
        } else {
            input.toString()
        }

        // Normalize: lowercase + remove all whitespace
        val lowerStr = rawString.lowercase(Locale.ROOT)
        val cleaned = deleteAllWhitespace(lowerStr)

        // Return null if cleaned string is empty
        if (cleaned.isEmpty()) return null

        // Check if the cleaned string contains only lowercase ASCII or char(23)
        for (char in cleaned) {
            if (!isLowerCaseAscii(char) && char.code != 23) {
                // If not, convert using IConvert and re-clean
                val converted = converter.convert(lowerStr)
                return if (converted == null) null else deleteAllWhitespace(converted)
            }
        }

        // All characters are valid; return the cleaned version
        return cleaned
    }


    private fun getLabelComparable(label: String): LabelComparable {
        val charInsertionPositions = mutableListOf<Int>()
        val processor = CharacterProcessor(0.toByte(), 0.toByte(), charInsertionPositions)
        var index = 0
        while (index < label.length) {
            val codePoint: Int = Character.codePointAt(label, index)
            processor.invoke(index, codePoint)
            index += Character.charCount(codePoint)
        }

        if (charInsertionPositions.isEmpty()) {
            return LabelComparable(label, emptyList())
        }

        // Start with a mutable string builder initialized from the input label
        val resultBuilder = StringBuilder(label)

        // 'v' is an offset tracker that increases with each inserted character
        var offset = 0

        // Insert the control character '\u0017' (End of Transmission Block) at specified positions
        for (pos in charInsertionPositions) {
            // Ensure the object is treated as a number, then calculate the adjusted index
            val insertIndex = (pos as Number).toInt() + offset

            // Insert the control character at the adjusted index
            resultBuilder.insert(insertIndex, '\u0017')

            // Increment offset to account for the new character just inserted
            offset++
        }
        return LabelComparable(resultBuilder.toString(), charInsertionPositions)
    }

    private fun initLocaleAlphabetMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locales.addAll(convertLocaleListToArrayList(LocaleList.getDefault()))
        } else {
            locales.add(Locale.getDefault())
        }
        localeAlphabetMap.clear()
        for (locale in locales.asReversed()) {
            when (locale.language) {
                "da" -> {
                    localeAlphabetMap['æ'] = LocaleAlphabetEnum.DISTINCT
                    localeAlphabetMap['ø'] = LocaleAlphabetEnum.DISTINCT
                    localeAlphabetMap['å'] = LocaleAlphabetEnum.DISTINCT
                }

                "de" -> {
                    localeAlphabetMap['ä'] = LocaleAlphabetEnum.WITH_LETTER
                    localeAlphabetMap['ö'] = LocaleAlphabetEnum.WITH_LETTER
                    localeAlphabetMap['ü'] = LocaleAlphabetEnum.WITH_LETTER
                }

                "es", "fil" -> {
                    localeAlphabetMap['ñ'] = LocaleAlphabetEnum.DISTINCT
                }

                "fi", "sv" -> {
                    localeAlphabetMap['å'] = LocaleAlphabetEnum.DISTINCT
                    localeAlphabetMap['ä'] = LocaleAlphabetEnum.DISTINCT
                    localeAlphabetMap['ö'] = LocaleAlphabetEnum.DISTINCT
                }

                "nb", "nn", "no" -> {
                    localeAlphabetMap['æ'] = LocaleAlphabetEnum.DISTINCT
                    localeAlphabetMap['ø'] = LocaleAlphabetEnum.DISTINCT
                    localeAlphabetMap['å'] = LocaleAlphabetEnum.DISTINCT
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertLocaleListToArrayList(localeList: LocaleList): ArrayList<Locale> {
        return ArrayList<Locale>().apply {
            for (i in 0 until localeList.size()) {
                add(localeList[i])
            }
        }
    }


    /**
     * Checks if a specific Transliterate ID is available, and adds it to the provided list if found.
     *
     * @param targetId The transliterate ID to search for (e.g., "Russian-Latin/BGN").
     * @param scriptList The mutable list where the found ID will be added.
     * @return True if the target ID was found and added, false otherwise.
     */
    @SuppressLint("NewApi")
    fun addTransliterateIfAvailable(targetId: String, scriptList: MutableList<String>): Boolean {
        val ids: Enumeration<String> = try {
            if (Build.VERSION.SDK_INT >= 29) {
                Transliterator.getAvailableIDs()
            } else {
                val method: Method? = Transliterator::class.java.getMethod("getAvailableIDs")
                @Suppress("UNCHECKED_CAST")
                method?.invoke(null) as? Enumeration<String> ?: Collections.emptyEnumeration()
            }
        } catch (e: Exception) {
            Collections.emptyEnumeration()
        }

        val idList = ids.toList()
        if (targetId in idList) {
            scriptList.add(targetId)
            return true
        }

        return false
    }
}