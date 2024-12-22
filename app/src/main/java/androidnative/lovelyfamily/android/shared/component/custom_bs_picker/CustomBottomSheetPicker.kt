package androidnative.lovelyfamily.android.shared.component.custom_bs_picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.CustomBottomSheetPickerBinding

interface CustomBottomSheetPickerInterface {
    fun showBottomSheet()
}

class CustomBottomSheetPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: CustomBottomSheetPickerBinding =
        CustomBottomSheetPickerBinding.inflate(LayoutInflater.from(context), this, true)

    private var customBottomSheetPickerInterface: CustomBottomSheetPickerInterface? = null

    init {
        setDefaultContent("Pilih item")

        binding.pickerLayout.setOnClickListener {
            customBottomSheetPickerInterface?.showBottomSheet()
        }
    }

    fun setTitle(value: String) {
        binding.title.text = value
    }

    fun setContent(value: String) {
        binding.content.text = value
        binding.content.setTextColor(ContextCompat.getColor(context, R.color.grey1))
    }

    fun setDefaultContent(value: String) {
        binding.content.text = value
        binding.content.setTextColor(ContextCompat.getColor(context, R.color.grey3))
    }

    fun setError(message: String?) {
        binding.error.text = message
        binding.error.visibility = if (message != null) VISIBLE else GONE
        binding.pickerLayout.setBackgroundResource(if (message != null) R.drawable.error_input_border else R.drawable.input_border)
    }

    fun setAction(customBottomSheetPickerInterface: CustomBottomSheetPickerInterface) {
        this.customBottomSheetPickerInterface = customBottomSheetPickerInterface
    }
}