package com.example.thedogapp.presentationlayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.thedogapp.R.id
import com.example.thedogapp.R.layout
import com.example.thedogapp.presentationlayer.models.DogUiModel


class DogListAdapter(private val itemClickListener: ItemClickListener) : PagingDataAdapter<DogUiModel, DogListAdapter.DogViewHolder>(
    DOG_DIFF_CALLBACK
) {
    private var isListView: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_LIST) layout.item_dog_list_layout else layout.item_dog_grid_layout
        val itemView = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return DogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)

        if (dog != null) {
            holder.imageViewDog.load(dog.imageUrl) {
                transformations(RoundedCornersTransformation(25F))
            }
            holder.textViewDogName.text = dog.name
            holder.imageViewDog.setOnClickListener {
                itemClickListener.onClick(dog)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isListView) VIEW_TYPE_LIST else VIEW_TYPE_GRID
    }

    fun setListViewMode(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged()
    }

    class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewDog: ImageView = itemView.findViewById(id.dogImage)
        val textViewDogName: TextView = itemView.findViewById(id.dogName)
    }

    companion object {
        private const val VIEW_TYPE_LIST = 1
        private const val VIEW_TYPE_GRID = 2
        private val DOG_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DogUiModel>() {
            override fun areItemsTheSame(oldItem: DogUiModel, newItem: DogUiModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DogUiModel, newItem: DogUiModel): Boolean =
                oldItem == newItem
        }
    }
}
