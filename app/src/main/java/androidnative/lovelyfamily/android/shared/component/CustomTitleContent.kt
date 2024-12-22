package androidnative.lovelyfamily.android.shared.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.CustomTitleContentBinding

class CustomTitleContent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomTitleContentBinding =
        CustomTitleContentBinding.inflate(LayoutInflater.from(context), this, true)

    fun setContent(title: String, content: String) {
        binding.title.text = title
        binding.content.text = content
    }

    fun setTextLayout(
        titleSize: Float = 12F, contentSize: Float = 14F,
        contentColor: Int = R.color.grey1, titleFont: Int = R.font.sf_pro_400,
        textAlignment: Int = TEXT_ALIGNMENT_TEXT_START
    ) {
        binding.title.textSize = titleSize
        binding.title.setTypeface(ResourcesCompat.getFont(context, titleFont))
        binding.content.textSize = contentSize
        binding.content.setTextColor(ResourcesCompat.getColor(context.resources, contentColor, null))
        binding.title.textAlignment = textAlignment
        binding.content.textAlignment = textAlignment

    }
}