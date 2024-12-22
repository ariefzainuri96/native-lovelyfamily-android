package androidnative.lovelyfamily.android.shared.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidnative.lovelyfamily.android.databinding.CustomAppbarBsBinding

@SuppressLint("CustomViewStyleable")
class CustomAppbarBS @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomAppbarBsBinding =
        CustomAppbarBsBinding.inflate(LayoutInflater.from(context), this, true)

    fun setLayout(title: String, action1: Drawable? = null) {
        binding.title.text = title
        binding.icAction1.setImageDrawable(action1)

        if (action1 != null) binding.icAction1.visibility = VISIBLE
    }

    fun setIcCloseClick(click: OnClickListener) {
        binding.icClose.setOnClickListener(click)
    }

    fun setIcAction1Click(click: OnClickListener) {
        binding.icAction1.setOnClickListener(click)
    }
}