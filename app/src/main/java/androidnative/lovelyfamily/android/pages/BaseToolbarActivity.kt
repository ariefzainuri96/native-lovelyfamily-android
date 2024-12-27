package androidnative.lovelyfamily.android.pages

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseToolbarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Child classes should call these methods in their `onCreate` after `setContentView`.
        setPaddingStatusBar()
        setToolbar()
        setContentView()
    }

    // Abstract method to enforce implementation in child classes
    abstract fun setPaddingStatusBar()

    abstract fun setContentView()

    // Abstract method for setting up the toolbar
    abstract fun setToolbar()

    protected fun implementContentView(layout: ViewGroup) {
        setContentView(layout)

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    // Reusable logic for applying window insets padding
    protected fun implementPaddingStatusBar(
        layout: ViewGroup, left: Int = 0, top: Int = 0,
        right: Int = 0, bottom: Int = 0
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(layout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + left.toInt(), systemBars.top + top.toInt(), systemBars
                    .right + right.toInt(), systemBars.bottom + bottom.toInt()
            )
            insets
        }
    }

    // Reusable logic for setting up the toolbar
    protected fun implementToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}