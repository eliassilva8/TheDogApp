package com.example.thedogapp.presentationlayer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.thedogapp.R

class DogListAdapter() : PagingDataAdapter<DogUiModel, DogListAdapter.DogViewHolder>(DOG_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)

        if (dog != null) {
            holder.imageViewDog.load(dog.imageUrl)
            holder.textViewDogName.text = dog.name
        }
    }

    class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewDog: ImageView = itemView.findViewById(R.id.dogImage)
        val textViewDogName: TextView = itemView.findViewById(R.id.dogName)
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
