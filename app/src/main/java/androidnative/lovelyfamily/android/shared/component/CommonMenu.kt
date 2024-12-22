package androidnative.lovelyfamily.android.shared.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidnative.lovelyfamily.android.databinding.CommonMenuBinding

interface CommonMenuInterface {
    fun handleOnClick()
}

class CommonMenu @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private var commonMenuInterface: CommonMenuInterface? = null
    private var binding: CommonMenuBinding = CommonMenuBinding.inflate(LayoutInflater.from(context), this, true)

    fun setTitle(value: String) {
        binding.title.text = value
    }

    fun setInterface(commonMenuInterface: CommonMenuInterface) {
        this.commonMenuInterface = commonMenuInterface
    }

    init {
        binding.root.setOnClickListener {
            commonMenuInterface?.handleOnClick()
        }
    }
}