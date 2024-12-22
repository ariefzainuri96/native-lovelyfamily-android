package androidnative.lovelyfamily.android.shared.component.custom_bs_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidnative.lovelyfamily.android.databinding.CustomBsPickerContentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface CustomBSPickerContentInterface {
    fun onRecyclerViewReady(adapter: RecyclerView.Adapter<*>?)
    fun onItemClick(position: Int)
    fun onGetMoreData()
}

class CustomBSPickerContent(
    private val title: String,
    private val customBSPickerContentInterface: CustomBSPickerContentInterface
) : BottomSheetDialogFragment() {
    lateinit var binding: CustomBsPickerContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomBsPickerContentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding.title.text = title

        // set adapter
        val adapter = CustomBSPickerContentAdapter()

        adapter.setInterface(object : CustomBSPickerContentAdapterInterface {
            override fun handleOnClick(position: Int) {
                customBSPickerContentInterface.onItemClick(position)
                dismiss()
            }
        })

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 >= totalItemCount) {
                    // Reached the end, load more data
                    customBSPickerContentInterface.onGetMoreData()
                }
            }
        })

        customBSPickerContentInterface.onRecyclerViewReady(binding.recyclerView.adapter)
    }
}