package androidnative.lovelyfamily.android.pages.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.ActivityLoginBinding
import androidnative.lovelyfamily.android.pages.dashboard.DashboardActivity
import androidnative.lovelyfamily.android.utils.RequestState
import androidnative.lovelyfamily.android.utils.Utils
import androidnative.lovelyfamily.android.utils.ValidationType
import androidnative.lovelyfamily.android.utils.collectLatestLifeCycleFlow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        eventHandler()

        setupView()
    }

    private fun setupView() {
        val fullText = "Don't have an account? Sign up"
        val spannableString = SpannableString(fullText)

        val signUpText = "Sign up"
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), // Set style (bold + italic)
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set click listener for "Sign up"
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                println("Sign up clicked")
            }

            override fun updateDrawState(ds: android.text.TextPaint) {
                // Remove underline and keep the custom color
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.black)
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Enable click on TextView
        binding.dontHaveAccount.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.dontHaveAccount.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }

    private fun eventHandler() {
        collectLatestLifeCycleFlow(viewModel.loginForm) {
            // so something
        }

        collectLatestLifeCycleFlow(viewModel.loginState) {
            if (it == RequestState.SUCCESS) {
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                finish()
            }
        }

        binding.usernameInput.apply {
            getTextInput().doOnTextChanged { text, _, _, _ ->
                viewModel.updateLoginForm { copy(username = text.toString()) }

                setError(
                    Utils.Companion.commonInputValidator(
                        text.toString(),
                        ValidationType.EMAIL
                    )
                )
            }
        }

        binding.passwordInput.apply {
            getTextInput().doOnTextChanged { text, _, _, _ ->
                viewModel.updateLoginForm { copy(password = text.toString()) }

                setError(
                    Utils.Companion.commonInputValidator(
                        text.toString(),
                        ValidationType.PASSWORD
                    )
                )
            }
        }
    }

}