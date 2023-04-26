package dev.thorcode.storyapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.thorcode.storyapp.api.ListStory
import dev.thorcode.storyapp.databinding.ListItemStoryBinding
import java.text.SimpleDateFormat
import java.util.*

class ListStoryAdapter(private val onItemClickListener: OnItemClickListener) : ListAdapter<ListStory, ListStoryAdapter.ViewHolder>(
    DiffUtils
) {
    class ViewHolder(val binding: ListItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.tvName.text = item?.name
        Glide.with(holder.itemView.context)
            .load(item?.photoUrl)
            .into(holder.binding.ivImage)
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = originalFormat.parse(item?.createdAt ?: "")
        holder.binding.tvCreatedAt.text = date?.let { targetFormat.format(it) }
        holder.binding.tvDescription.text = item?.description

        onItemClickListener.let {
            holder.itemView.setOnClickListener {
                onItemClickListener.onClickListener(item?.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    companion object DiffUtils : DiffUtil.ItemCallback<ListStory>() {
        override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory) =
            oldItem.id == newItem.id
    }

    interface OnItemClickListener {
        fun onClickListener(id: String)
    }
}