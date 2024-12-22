package androidnative.lovelyfamily.android.shared.component

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.CustomErrorBinding

class CustomError @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val binding = CustomErrorBinding.inflate(LayoutInflater.from(context), this, true)
    var handleClick: (() -> Unit)? = null

    fun setError(error: String) {
        binding.error.text = error

        setTextColor(error)
    }

    private fun setTextColor(error: String = "Gagal mendapatkan data") {
        // The full text
        val fullText = "${error}, Retry?"

        // Create a SpannableString
        val spannableString = SpannableString(fullText)

        // Find the index of the word "Retry?"
        val startIndex = fullText.indexOf("Retry?")
        val endIndex = startIndex + "Retry?".length

        // Change text color for "Retry?"
        val colorSpan =
            ForegroundColorSpan(ResourcesCompat.getColor(context.resources, R.color.primary, null))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Add a click listener for "Retry?"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                handleClick?.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // Remove underline
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the SpannableString to the TextView
        binding.error.text = spannableString

        // Enable clicking on the TextView
        binding.error.movementMethod = LinkMovementMethod.getInstance()
        binding.error.highlightColor = Color.TRANSPARENT
    }

    fun setLayout(textColor: Int = R.color.grey1, iconColor: Int = R.color.grey1) {
        binding.error.setTextColor(ResourcesCompat.getColor(context.resources, textColor, null))
        ImageViewCompat.setImageTintList(
            binding.icon,
            ResourcesCompat.getColorStateList(context.resources, iconColor, null)
        )
    }

    fun setHandleClickListener(handleClick: () -> Unit) {
        this.handleClick = handleClick
    }

    init {
        setError("Gagal mendapatkan data")
    }
}