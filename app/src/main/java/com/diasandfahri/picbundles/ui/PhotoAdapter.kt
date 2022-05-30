package com.diasandfahri.picbundles.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diasandfahri.picbundles.R
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.databinding.RowItemPhotoBinding

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: RowItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val photoList = ArrayList<PhotoItem>()
    val photos get() = photoList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        RowItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.binding.apply {
            val photo = photoList[position]
            Glide.with(rowImage)
                .load(photo.urls?.regular)
                .placeholder(R.drawable.ic_broken)
                .error(R.drawable.ic_broken)
                .into(rowImage)
        }
    }


    override fun getItemCount(): Int = photoList.size

    fun setData(data: List<PhotoItem>?) {
        data?.let {
            photoList.clear()
            photoList.addAll(data)
        }
    }

}