package iberoplast.pe.gespro.util

import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import androidx.appcompat.app.AppCompatActivity

object ActionBarUtils {

    private const val TITLE_SIZE = 15
    private const val SUBTITLE_SIZE = 11

    fun setCustomTitle(activity: AppCompatActivity, title: String, subtitle: String? = null) {
        val uppercaseTitle = title.toUpperCase()
        val uppercaseSubtitle = subtitle?.toUpperCase()

        activity.supportActionBar?.apply {
            this.title = getSpannableString(uppercaseTitle, TITLE_SIZE)
            uppercaseSubtitle?.let { nonNullSubtitle ->
                this.subtitle = getSpannableString(nonNullSubtitle, SUBTITLE_SIZE)
            }
        }
    }

    private fun getSpannableString(text: String, textSize: Int): CharSequence {
        val spannableString = SpannableString(text)
        spannableString.setSpan(AbsoluteSizeSpan(textSize, true), 0, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannableString
    }
}