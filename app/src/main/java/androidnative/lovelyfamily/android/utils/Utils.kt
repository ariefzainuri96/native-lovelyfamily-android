package androidnative.lovelyfamily.android.utils

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.shared.model.FormErrorModel
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import kotlin.random.Random
import kotlin.reflect.full.memberProperties

enum class ValidationType {
    EMAIL, PASSWORD, STANDART
}

class Utils {
    companion object {

        fun generateRandomString(minLength: Int, maxLength: Int): String {
            require(minLength >= 0) { "Minimum length must be non-negative" }
            require(maxLength >= minLength) { "Maximum length must be greater than or equal to minimum length" }

            val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Alphanumeric characters
            val length = Random.nextInt(minLength, maxLength + 1)

            return (1..length)
                .map { charPool.random() }
                .joinToString("")
        }

        fun commonInputValidator(message: String, inputType: ValidationType): String? {
            return when (inputType) {
                ValidationType.EMAIL -> {
                    if (message.isEmpty()) "Input can't be empty"
                    else null
                }

                ValidationType.PASSWORD -> {
                    if (message.isEmpty()) "Password can't be empty"
                    else null
                }

                ValidationType.STANDART -> {
                    if (message.isEmpty()) "Input can't be empty"
                    else null
                }
            }
        }

        fun setStatusBarTransparent(activity: Activity, view: View) {
            activity.apply {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

                // Ensure the content extends into the status bar area
                WindowCompat.setDecorFitsSystemWindows(window, false)

                // Check if running on Android 30+ (API 30 / Android 11)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.windowInsetsController?.apply {
                        // Ensure the status bar is visible
                        show(WindowInsets.Type.statusBars())
                    }

                    // Create a custom background for the status bar
                    val rootView = window.decorView.findViewById<View>(android.R.id.content)
                    rootView.setOnApplyWindowInsetsListener { view, insets ->
                        val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                        view.setPadding(
                            view.paddingLeft,
                            statusBarInsets.top, // Apply padding for status bar
                            view.paddingRight,
                            view.paddingBottom
                        )
                        insets
                    }
                    rootView.setBackgroundColor(ContextCompat.getColor(this, R.color.defaultStatusBar)) // Set the desired status bar background color
                } else {
                    // Fallback for older versions
                    @Suppress("DEPRECATION")
                    window.statusBarColor = ContextCompat.getColor(this, R.color.defaultStatusBar)

                    ViewCompat.setOnApplyWindowInsetsListener(view) { root, windowInset ->
                        val inset = windowInset.getInsets(WindowInsetsCompat.Type.systemBars())
                        root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            leftMargin = inset.left
                            bottomMargin = inset.bottom
                            rightMargin = inset.right
                            topMargin = 0
                        }
                        WindowInsetsCompat.CONSUMED
                    }
                }
            }
        }

        fun setStatusBarShown(activity: Activity, view: View, top: Int? = 0) {
            activity.apply {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

                WindowCompat.setDecorFitsSystemWindows(window, false)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.windowInsetsController?.apply {
                        // Ensure the status bar is visible
                        show(WindowInsets.Type.statusBars())
                    }

                    // Create a custom background for the status bar
                    val rootView = window.decorView.findViewById<View>(android.R.id.content)
                    rootView.setOnApplyWindowInsetsListener { view, insets ->
                        val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                        view.setPadding(
                            view.paddingLeft,
                            statusBarInsets.top, // Apply padding for status bar
                            view.paddingRight,
                            view.paddingBottom
                        )
                        insets
                    }
                    rootView.setBackgroundColor(ContextCompat.getColor(this, R.color.defaultStatusBar)) // Set the desired status bar background color
                } else {
                    // Fallback for older versions
                    @Suppress("DEPRECATION")
                    window.statusBarColor = ContextCompat.getColor(this, R.color.defaultStatusBar)

                    ViewCompat.setOnApplyWindowInsetsListener(view) { root, windowInset ->
                        val inset = windowInset.getInsets(WindowInsetsCompat.Type.statusBars())
                        root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            leftMargin = inset.left
                            bottomMargin = inset.bottom
                            topMargin = top ?: inset.top
                            rightMargin = inset.right
                        }
                        WindowInsetsCompat.CONSUMED
                    }
                }

                // For older Android versions, this is not directly supported
                // You may need to set a dark status bar background to make icons appear white
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    false
            }
        }

        fun getWindowHeight(activity: FragmentActivity?): Int {
            val displayMetrics = DisplayMetrics()

            activity?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    @Suppress("DEPRECATION")
                    it.display?.getRealMetrics(displayMetrics)
                } else {
                    @Suppress("DEPRECATION")
                    it.windowManager.defaultDisplay.getMetrics(displayMetrics)
                }
            }

            return displayMetrics.heightPixels
        }

        // Extension function to check all properties of any data class
        inline fun <reified T : Any> T.checkAllProperties(): List<FormErrorModel> {
            val errors = mutableListOf<FormErrorModel>()

            for (property in T::class.memberProperties) {
                val value = property.get(this)
                if (value is String? && value.isNullOrEmpty()) {
                    errors.add(FormErrorModel(key = property.name, error =
                    "${convertCamelToTitle(property.name)} tidak boleh kosong"))
                }
            }

            return errors
        }

        inline fun <reified T : Any> T.isObjectEmpty(): Boolean {
            for (property in T::class.memberProperties) {
                val value = property.get(this)
                if (value is String? && !value.isNullOrEmpty()) {
                    return false
                }
            }

            return true
        }

        fun convertCamelToTitle(camelCase: String): String {
            // Use a regular expression to find the capital letters and insert spaces before them
            val titleCase = camelCase.replace(Regex("([a-z])([A-Z])"), "$1 $2")
            // Capitalize the first letter of each word
            return titleCase.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
        }
    }
}