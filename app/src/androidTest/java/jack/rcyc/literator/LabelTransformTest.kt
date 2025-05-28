package jack.rcyc.literator

import androidx.test.ext.junit.runners.AndroidJUnit4
import jack.rcyc.labeltransliterator.LabelTransform
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.Test

import org.junit.Before


@RunWith(AndroidJUnit4::class)
class LabelTransformTest {

    private var labelTransLiterator: LabelTransform? = null

    @Before
    fun setUp() {
        labelTransLiterator = LabelTransform() // adjust if constructor requires params
    }

    @Test
    fun testEnglish() {
        assertEquals("Z", labelTransLiterator!!.getRepresentLetter("zip"))
        assertEquals("A", labelTransLiterator!!.getRepresentLetter("apple"))
    }

    @Test
    fun testChinese() {
        // 中 (zhōng) -> Z
        assertEquals("Z", labelTransLiterator!!.getRepresentLetter("中"))
        // 北京 (běi jīng) -> B
        assertEquals("B", labelTransLiterator!!.getRepresentLetter("北京"))
    }

    @Test
    fun testJapanese() {
        // こんにちは -> K (kon'nichiwa)
        assertEquals("K", labelTransLiterator!!.getRepresentLetter("こんにちは"))
        // カタカナ -> K (katakana)
        assertEquals("K", labelTransLiterator!!.getRepresentLetter("カタカナ"))
    }

    @Test
    fun testKorean() {
        // 한글 -> H (hangeul)
        assertEquals("H", labelTransLiterator!!.getRepresentLetter("한글"))
    }

    @Test
    fun testRussian() {
        // Москва (Moskva) -> M
        assertEquals("M", labelTransLiterator!!.getRepresentLetter("Москва"))
    }

    @Test
    fun testArabic() {
        // سلام -> S (salaam)
        assertEquals("S", labelTransLiterator!!.getRepresentLetter("سلام"))
    }

    @Test
    fun testGreek() {
        // Αθήνα (Athína) -> A
        assertEquals("A", labelTransLiterator!!.getRepresentLetter("Αθήνα"))
    }

    @Test
    fun testHebrew() {
        // שלום (Shalom) -> S
        assertEquals("S", labelTransLiterator!!.getRepresentLetter("שלום"))
    }

    @Test
    fun testRomanian() {
        // țară -> T
        assertEquals("T", labelTransLiterator!!.getRepresentLetter("țară"))
        // România -> R
        assertEquals("R", labelTransLiterator!!.getRepresentLetter("România"))
    }

    @Test
    fun testMixedCaseAndSymbols() {
        assertEquals("A", labelTransLiterator!!.getRepresentLetter("   apple"))
        assertEquals("#", labelTransLiterator!!.getRepresentLetter("@banana"))
        assertEquals("C", labelTransLiterator!!.getRepresentLetter("Çilek")) // Turkish C -> C
    }

    @Test
    fun testEmptyAndNullInput() {
        assertEquals("#", labelTransLiterator!!.getRepresentLetter("")) // assuming # for fallback
        assertEquals("#", labelTransLiterator!!.getRepresentLetter(null))
    }
}