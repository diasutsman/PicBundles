package com.diasandfahri.picbundles.ui

import androidx.recyclerview.widget.DiffUtil
import com.diasandfahri.picbundles.data.response.PhotoItem

class DiffCallback(private val oldList: List<PhotoItem>, private val newList: List<PhotoItem>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // are the reference all the same
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        // are the content are completely the same
        return oldData.id == newData.id
                && oldData.urls?.regular == newData.urls?.regular
                && oldData.user?.fullName == newData.user?.fullName
                && oldData.user?.profileImage?.small == newData.user?.profileImage?.small
                && oldData.user?.fullName == newData.user?.fullName

    }
}