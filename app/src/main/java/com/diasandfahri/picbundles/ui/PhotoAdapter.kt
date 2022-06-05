package com.diasandfahri.picbundles.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.doOnLayout
import androidx.databinding.adapters.ViewBindingAdapter.setOnLongClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.diasandfahri.picbundles.R
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.databinding.RowItemPhotoBinding
import com.diasandfahri.picbundles.ui.detail.DetailActivity
import com.diasandfahri.picbundles.utils.ExtendedFunctions.getLifeCycleOwner

class PhotoAdapter(private val viewModel: PhotoViewModel) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(
        private val binding: RowItemPhotoBinding,
        private val listener: (Int) -> Unit,
        private val longClickListener: (View, Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                setOnClickListener {
                    listener(adapterPosition)
                }
                setOnLongClickListener {
                    longClickListener(it, adapterPosition)
                    true
                }
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
        ),
        { position ->
            parent.context.startActivity(
                Intent(
                    parent.context,
                    DetailActivity::class.java
                ).putExtra(DetailActivity.PHOTO_KEY, photoList[position])
            )
        },
        { view, position ->
            val photo = photoList[position]
            val isBookmarkedLiveData = viewModel.isBookmarked(photo)
            isBookmarkedLiveData.observe(parent.context.getLifeCycleOwner() as AppCompatActivity) { isBookmarked ->
                val popup = PopupMenu(parent.context, view)
                popup.inflate(if (isBookmarked) R.menu.unbookmark_menu else R.menu.bookmark_menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_bookmark -> {
                            viewModel.bookmarkPhoto(photo)
                            Toast.makeText(parent.context,
                                parent.context.getString(R.string.txt_bookmark_added),
                                Toast.LENGTH_SHORT)
                                .show()
                        }

                        R.id.action_unbookmark -> {
                            viewModel.unBookmarkPhoto(photo)
                            Toast.makeText(parent.context,
                                parent.context.getString(R.string.txt_bookmark_removed),
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    true
                }
                popup.show()
                isBookmarkedLiveData.removeObservers(parent.context.getLifeCycleOwner() as AppCompatActivity)
            }
        }
    )

    override fun onBindViewHolder(holder: PhotoAdapter.PhotoViewHolder, position: Int) {
        val photo = photoList[position]
        holder.bind(photo)
    }

    fun getPhotoAt(position: Int) = photoList[position]

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