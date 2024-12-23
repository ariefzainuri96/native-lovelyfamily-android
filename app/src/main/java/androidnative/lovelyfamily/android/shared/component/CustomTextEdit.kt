package androidnative.lovelyfamily.android.shared.component

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.CustomTextEditBinding
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomTextEdit @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomTextEditBinding =
        CustomTextEditBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        // Retrieve custom attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextEdit)
        val hint = typedArray.getString(R.styleable.CustomTextEdit_hint)
        val obscure = typedArray.getBoolean(R.styleable.CustomTextEdit_obscure, false)
        val action = typedArray.getInt(
            R.styleable.CustomTextEdit_android_imeOptions,
            EditorInfo.IME_ACTION_DONE
        )
        val borderColor = typedArray.getInt(
            R.styleable.CustomTextEdit_borderColor, ContextCompat
                .getColor(context, R.color.primary)
        )
        val inputType = typedArray.getInt(
            R.styleable.CustomTextEdit_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )
        val maxLine = typedArray.getInt(R.styleable.CustomTextEdit_android_maxLines, 1)
        val minLine = typedArray.getInt(R.styleable.CustomTextEdit_android_minLines, 1)
        typedArray.recycle()

        // set layout
        setBoxStrokeColorSelector(borderColor)

        // Set text for TextViews
        binding.inputLayout.hint = hint
        binding.textInput.imeOptions = action
        setTextLayout(minLine, maxLine)
        binding.textInput.inputType = inputType

        if (obscure) {
            binding.inputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            binding.textInput.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun setBoxStrokeColorSelector(borderColor: Int) {
        //Color from hex string
        val defaultColor = ContextCompat.getColor(context, R.color.black)
        val error = ContextCompat.getColor(context, R.color.red4)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_focused),  // focused
            intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf() // default
        )

        val colors = intArrayOf(
            borderColor, // focused color
            defaultColor, // enabled color
            defaultColor
        ) // default color

        val errorColorList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(error))
        val myColorList = ColorStateList(states, colors)

        binding.inputLayout.setErrorIconTintList(errorColorList)
        binding.inputLayout.setErrorTextColor(errorColorList)
        binding.inputLayout.boxStrokeErrorColor = errorColorList
        binding.inputLayout.setBoxStrokeColorStateList(myColorList)
    }

    fun setTextEnabled(enabled: Boolean) {
        binding.textInput.isEnabled = enabled
    }

    fun setTextLayout(minLine: Int = 1, maxLine: Int = 1) {
        binding.textInput.minLines = minLine
        binding.textInput.maxLines = maxLine

        if (minLine > 1 || maxLine > 1) {
            binding.textInput.gravity = Gravity.TOP or Gravity.START
        }
    }

    fun getTextInput(): TextInputEditText {
        return binding.textInput
    }

    fun setTextInput(text: String) {
        binding.textInput.setText(text)
    }

    fun setError(message: String?) {
        binding.inputLayout.error = message
        binding.inputLayout.isErrorEnabled = message != null
    }
}