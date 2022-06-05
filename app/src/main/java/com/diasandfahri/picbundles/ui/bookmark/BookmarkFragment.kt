package com.diasandfahri.picbundles.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.diasandfahri.picbundles.databinding.FragmentBookmarkBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding as FragmentBookmarkBinding

    private val viewModel: PhotoViewModel by activityViewModels()

    private val mAdapter by lazy {
        PhotoAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        setupRecyclerView()

        viewModel.getAllBookmarkedPhotos().observe(viewLifecycleOwner) {
            mAdapter.setData(it)
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvBookmark.apply {
            adapter = mAdapter
            ItemTouchHelper(SwipeToDelete(viewModel, mAdapter)).attachToRecyclerView(this)
        }
    }

    // prevent memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}