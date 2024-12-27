package androidnative.lovelyfamily.android.pages.new_activity

import android.os.Bundle
import androidnative.lovelyfamily.android.databinding.ActivityNewBinding
import androidnative.lovelyfamily.android.pages.BaseToolbarActivity

class NewActivity : BaseToolbarActivity() {
    private lateinit var binding: ActivityNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        place binding above `super.onCreate` to ensure that binding is initialized before used in
        [setContentView]
        */
        binding = ActivityNewBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
    }

    override fun setPaddingStatusBar() = implementPaddingStatusBar(binding.toolbarLayout)
    override fun setContentView() = implementContentView(binding.root)
    override fun setToolbar() = implementToolbar(binding.toolbar)
}