package com.diasandfahri.picbundles.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.diasandfahri.picbundles.databinding.FragmentBookmarkBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding as FragmentBookmarkBinding

    private val viewModel: PhotoViewModel by activityViewModels()

    private val adapter by lazy {
        PhotoAdapter(viewModel)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        binding.rvBookmark.adapter = adapter

        viewModel.getAllBookmarkedPhotos().observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        return binding.root
    }
}