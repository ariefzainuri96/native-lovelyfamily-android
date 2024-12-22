package androidnative.lovelyfamily.android.shared.component

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.CustomTextEditBinding

class CustomTextEdit @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomTextEditBinding =
        CustomTextEditBinding.inflate(LayoutInflater.from(context), this, true)

    private var error: String? = null

    init {
        // Retrieve custom attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextEdit)
        val title = typedArray.getString(R.styleable.CustomTextEdit_title)
        val hint = typedArray.getString(R.styleable.CustomTextEdit_hint)
        val obscure = typedArray.getBoolean(R.styleable.CustomTextEdit_obscure, false)
        val action = typedArray.getInt(
            R.styleable.CustomTextEdit_android_imeOptions,
            EditorInfo.IME_ACTION_DONE
        )
        val borderColor = typedArray.getInt(
            R.styleable.CustomTextEdit_borderColor, ContextCompat
                .getColor(context, R.color.input_border)
        )
        val inputType = typedArray.getInt(
            R.styleable.CustomTextEdit_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )
        val maxLines = typedArray.getInt(R.styleable.CustomTextEdit_android_maxLines, 1)
        typedArray.recycle()

        // set layout
        // setBoxStrokeColorSelector(borderColor, binding)

        // Set text for TextViews
        binding.title.text = title
        binding.textInput.hint = hint
        binding.textInput.imeOptions = action
        binding.textInput.maxLines = maxLines
        binding.textInput.inputType = inputType

        if (obscure) {
            binding.textInput.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        binding.textInput.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.inputLayout.setBackgroundResource(R.drawable.focused_input_border)
            } else {
                if (error != null) {
                    binding.inputLayout.setBackgroundResource(R.drawable.error_input_border)
                } else {
                    binding.inputLayout.setBackgroundResource(R.drawable.input_border)
                }

            }
        }
    }

//    private fun setBoxStrokeColorSelector(borderColor: Int?, binding: CustomTextEditBinding) {
//        //Color from hex string
//        val defaultColor = ContextCompat.getColor(context, R.color.line)
//
//        val states = arrayOf(
//            intArrayOf(android.R.attr.state_focused),  // focused
//            intArrayOf(android.R.attr.state_hovered), // hovered
//            intArrayOf(android.R.attr.state_enabled), // enabled
//            intArrayOf() // default
//        )
//
//        val colors = intArrayOf(
//            borderColor ?: defaultColor, // focused color
//            borderColor ?: defaultColor, // hovered color
//            borderColor ?: defaultColor, // enabled color
//            borderColor ?: defaultColor
//        ) // default color
//
//        val myColorList = ColorStateList(states, colors)
//
//        binding.inputLayout.setBoxStrokeColorStateList(myColorList)
//    }

    fun setTextEnabled(enabled: Boolean) {
        binding.textInput.isEnabled = enabled
    }

    fun setTextLayout(minLine: Int = 1, maxLine: Int = 1) {
        binding.textInput.minLines = minLine
        binding.textInput.maxLines = maxLine
    }

    fun getTextInput(): TextInputEditText {
        return binding.textInput
    }

    fun setTextInput(text: String) {
        binding.textInput.setText(text)
    }

    fun setError(message: String?) {
        error = message
        binding.error.text = error
        binding.error.visibility = if (error != null) VISIBLE else GONE
        binding.inputLayout.setBackgroundResource(if (error != null) R.drawable.error_input_border
        else R.drawable.input_border)
    }
}