package com.diasandfahri.picbundles.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.databinding.RowItemPhotoBinding
import com.diasandfahri.picbundles.ui.detail.DetailActivity

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(
        private val binding: RowItemPhotoBinding,
        private val listener: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener(adapterPosition)
            }
        }

        fun bind(photo: PhotoItem) {
            binding.photo = photo
        }
    }

    private val photoList = ArrayList<PhotoItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        RowItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) {
        parent.context.startActivity(
            Intent(
                parent.context,
                DetailActivity::class.java
            ).putExtra(DetailActivity.PHOTO_KEY, photoList[it])
        )

    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) =
        holder.bind(photoList[position])


    override fun getItemCount(): Int = photoList.size

    fun setData(data: List<PhotoItem>?) {
        data?.let {
            val diffCallback = DiffCallback(photoList, data)
            val diffCallbackResult = DiffUtil.calculateDiff(diffCallback)
            photoList.clear()
            photoList.addAll(data)
            diffCallbackResult.dispatchUpdatesTo(this)
        }
    }

}