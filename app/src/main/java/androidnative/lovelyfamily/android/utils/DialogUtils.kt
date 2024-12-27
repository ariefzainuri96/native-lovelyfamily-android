package androidnative.lovelyfamily.android.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidnative.lovelyfamily.android.databinding.CustomLoadingDialogBinding

object DialogLoadingUtils {
    private var dialog: Dialog? = null

    fun showLoadingDialog(context: Context, title: String? = null) {
        if (dialog == null) {
            val binding = CustomLoadingDialogBinding.inflate(LayoutInflater.from(context))

            binding.title.text = title
            binding.title.visibility = if (title != null) View.VISIBLE else View.GONE

            dialog = Dialog(context).apply {
                setContentView(binding.root)
                setCancelable(false) // Prevent dialog from being canceled
                window?.setBackgroundDrawableResource(android.R.color.transparent) // Set transparent background
            }
        }

        if (dialog?.isShowing == false) {
            dialog?.show()
        }
    }

    fun dismissLoadingDialog() {
        dialog?.dismiss()
        dialog = null
    }
}