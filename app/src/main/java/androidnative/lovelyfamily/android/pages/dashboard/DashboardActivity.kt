package androidnative.lovelyfamily.android.pages.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.databinding.ActivityDashboardBinding
import androidnative.lovelyfamily.android.features.dashboard.adapter.MenuAdapter
import androidnative.lovelyfamily.android.features.dashboard.adapter.NewsViewPagerAdapter
import androidnative.lovelyfamily.android.features.dashboard.model.MenuModel
import androidnative.lovelyfamily.android.pages.BaseToolbarActivity
import androidnative.lovelyfamily.android.pages.login.LoginActivity
import androidnative.lovelyfamily.android.pages.new_activity.NewActivity
import androidnative.lovelyfamily.android.utils.RequestState
import androidnative.lovelyfamily.android.utils.cameraPermissionRequest
import androidnative.lovelyfamily.android.utils.collectLatestLifeCycleFlow
import androidnative.lovelyfamily.android.utils.dpToPx
import androidnative.lovelyfamily.android.utils.isPermissionGranted
import androidnative.lovelyfamily.android.utils.openPermissionSetting
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseToolbarActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    private val cameraPermission = android.Manifest.permission.CAMERA

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // do something
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        observeData()

        setupView()

        setMenuRecyclerView()
    }

    override fun setPaddingStatusBar() = implementPaddingStatusBar(binding.toolbarLayout, left =
    dpToPx(16f), right = dpToPx(16f), top = dpToPx(16f))

    override fun setContentView() = implementContentView(binding.root)

    override fun setToolbar() {}

    private fun requestCameraAndStartScanner() {
        if (isPermissionGranted(cameraPermission)) {
            // do something
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                cameraPermissionRequest {
                    openPermissionSetting()
                }
            }

            else -> {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }

    private fun setupView() {
        binding.profileLayout.setOnClickListener {
            viewModel.logout()
        }

        binding.scanQRLayout.setOnClickListener {
//            requestCameraAndStartScanner()
            startActivity(Intent(this, NewActivity::class.java))
        }

        binding.newsError.apply {
            setHandleClickListener { viewModel.getNews() }
//            setInterface(object : CustomErrorInterface {
//                override fun handleRetryClickListener() = viewModel.getNews()
//            })

            setLayout(textColor = R.color.white, iconColor = R.color.white)
        }
    }

    private fun observeData() {
        collectLatestLifeCycleFlow(viewModel.newsState) { state ->
            binding.newsLoading.visibility =
                if (state == RequestState.LOADING) View.VISIBLE else View.GONE
            binding.newsError.visibility =
                if (state == RequestState.ERROR) View.VISIBLE else View.GONE

            if (state == RequestState.SUCCESS) {
                setupNewsViewPager()
            }
        }

        collectLatestLifeCycleFlow(viewModel.logoutState) { state ->
            if (state == RequestState.SUCCESS) {
                val i = Intent(this, LoginActivity::class.java)
                // set the new task and clear flags
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }
    }

    private fun setMenuRecyclerView() {
        val adapter = MenuAdapter(viewModel.menus)

        adapter.setOnClickListener(object : MenuAdapter.OnClickListener {
            override fun onClick(
                position: Int,
                model: MenuModel
            ) {
                when (position) {
                    0 -> println("Manajemen Inventaris")
                    1 -> println("Manajemen Barang Pakai Habis")
                    2 -> println("Manajemen Aset")
                    3 -> println("Task Approval")
                }
            }
        })

        binding.menuRecyclerView.adapter = adapter

        binding.menuRecyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun setupNewsViewPager() {
        binding.newsViewPager.adapter = NewsViewPagerAdapter(viewModel.news.value)

        //set the orientation of the viewpager using ViewPager2.orientation
        binding.newsViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // registering for page change callback
        binding.newsViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // use position to get the page
                    println("newsPageChanged $position")
                }
            }
        )

        binding.newsViewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2

            //increase this offset to show more of left/right
            val offsetPx = 16.dpToPx(resources.displayMetrics)
            setPadding(offsetPx, 0, offsetPx, 0)

            //increase this offset to increase distance between 2 items
            val pageMarginPx = 8.dpToPx(resources.displayMetrics)
            setPageTransformer(MarginPageTransformer(pageMarginPx))
        }

        binding.newsIndicator.setViewPager(binding.newsViewPager)
    }
}