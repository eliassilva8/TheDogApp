package com.example.thedogapp.presentationlayer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.thedogapp.databinding.ItemDogSearchBinding

class DogSearchAdapter(private val itemClickListener: ItemClickListener) : PagingDataAdapter<DogUiModel, DogSearchAdapter.DogViewHolder>(DOG_DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = ItemDogSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)

        if (dog != null) {
            holder.dogImage.load(dog.imageUrl) {
                transformations(RoundedCornersTransformation(25F))
            }
            holder.dogName.text = dog.name
            holder.dogGroup.text = dog.group.ifBlank {
                holder.dogGroup.visibility = View.GONE
                ""
            }
            holder.dogOrigin.text = dog.origin.ifBlank {
                holder.dogOrigin.visibility = View.GONE
                ""
            }
            holder.container.setOnClickListener {
                itemClickListener.onClick(dog)
            }
        }
    }

    class DogViewHolder(binding: ItemDogSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        val container = binding.container
        val dogImage = binding.dogImage
        val dogName = binding.dogName
        val dogGroup = binding.dogGroup
        val dogOrigin = binding.dogOrigin
    }

    companion object {
        private val DOG_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DogUiModel>() {
            override fun areItemsTheSame(oldItem: DogUiModel, newItem: DogUiModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DogUiModel, newItem: DogUiModel): Boolean =
                oldItem == newItem
        }
    }
}
