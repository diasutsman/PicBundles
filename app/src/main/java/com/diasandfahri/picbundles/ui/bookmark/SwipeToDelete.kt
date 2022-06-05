package com.diasandfahri.picbundles.ui.bookmark

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel
import com.google.android.material.snackbar.Snackbar

class SwipeToDelete(private val viewModel: PhotoViewModel, private val adapter: PhotoAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val photo = adapter.getPhotoAt(position)

        viewModel.unBookmarkPhoto(photo)
        restoreDataIfUserUndo(viewHolder.itemView,photo)
    }

    private fun restoreDataIfUserUndo(view: View, photo: PhotoItem) {
        Snackbar.make(view, "Photo deleted", Snackbar.LENGTH_LONG)
            .setAction("undo") {
                viewModel.bookmarkPhoto(photo)
            }.show()
    }
}