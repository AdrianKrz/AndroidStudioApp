package com.example.prm4.adapers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prm4.R
import com.example.prm4.databinding.CarImageBinding

class DishImageViewHolder(val binding: CarImageBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(resId: Int, isSelected: Boolean) {
        binding.image.setImageResource(resId)
        binding.selectedFrame.visibility =
            if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class DishImagesAdapter : RecyclerView.Adapter<DishImageViewHolder>() {

    companion object {
        @JvmStatic
        var id: Int = 0
            private set(value) {
                field = value
            }

        @JvmStatic
        fun set(value: Int) {
            id = value
        }
    }

    private val images = listOf(R.drawable.mercedes, R.drawable.red_bulll, R.drawable.ferrari)

    private var selectedPosition: Int = id
    val selectedIdRes: Int
        get() = images[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishImageViewHolder {
        val binding = CarImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DishImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = vh.layoutPosition
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: DishImageViewHolder, position: Int) {
        holder.bind(images[position], position == selectedPosition)
}

    override fun getItemCount(): Int = images.size
}