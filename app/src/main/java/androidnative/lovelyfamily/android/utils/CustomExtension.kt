package androidnative.lovelyfamily.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> BottomSheetDialogFragment.collectLatestLifeCycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun Context.toPx(dp: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics).toInt()

@SuppressLint("ObsoleteSdkInt", "InternalInsetResource")
fun AppCompatActivity.getStatusBarHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            Resources.getSystem().getDimensionPixelSize(resourceId)
        } else {
            0
        }
    } else {
        // For pre-Marshmallow devices, calculate the height in dp
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}

fun Context.isLoggedIn(): Flow<Boolean?> {
    val isLogin = dataStore.data.map {
        it[PreferenceKeys.IS_LOGIN]
    }

    return isLogin
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

inline fun Context.cameraPermissionRequest(crossinline positive: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle("Camera Permission Required")
        .setMessage("Without accessing the camera it is not possible to SCAN QR Codes...")
        .setPositiveButton("Allow Camera") { dialog, which ->
            positive.invoke()
        }.setNegativeButton("Cancel") { dialog, which ->

        }.show()
}

fun Context.openPermissionSetting() {
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
        val uri: Uri = Uri.fromParts("package", packageName, null)
        it.data = uri
        startActivity(it)
    }
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

fun Context.pxToDp(px: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        px.toFloat() / resources.displayMetrics.density,
        resources.displayMetrics
    )
}

fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    ).toInt()
}