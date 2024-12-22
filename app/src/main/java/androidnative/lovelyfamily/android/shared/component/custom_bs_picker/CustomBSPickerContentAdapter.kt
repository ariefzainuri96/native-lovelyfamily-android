package androidnative.lovelyfamily.android.shared.component.custom_bs_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidnative.lovelyfamily.android.databinding.CustomBsPickerContentItemBinding
import androidx.recyclerview.widget.RecyclerView

interface CustomBSPickerContentAdapterInterface {
    fun handleOnClick(position: Int)
}

class CustomBSPickerContentAdapter :
    RecyclerView.Adapter<CustomBSPickerContentAdapter.ViewHolder>() {
    private var customBSPickerContentAdapterInterface: CustomBSPickerContentAdapterInterface? = null
    private var titleContent = mutableListOf<String>()

    inner class ViewHolder(val binding: CustomBsPickerContentItemBinding) : RecyclerView.ViewHolder(
        binding
            .root
    ) {
        fun setData(data: String) {
            binding.content.text = data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = CustomBsPickerContentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.setData(titleContent[position])

        holder.binding.root.setOnClickListener {
            customBSPickerContentAdapterInterface?.handleOnClick(position)
        }
    }

    override fun getItemCount(): Int = titleContent.size

    fun setInterface(customBSPickerContentAdapterInterface: CustomBSPickerContentAdapterInterface) {
        this.customBSPickerContentAdapterInterface = customBSPickerContentAdapterInterface
    }

    fun updateData(titles: List<String>) {
        val previousSize = titleContent.size
        titleContent.clear()
        titleContent.addAll(titles)
        notifyItemRangeInserted(previousSize, titleContent.size)
    }
}