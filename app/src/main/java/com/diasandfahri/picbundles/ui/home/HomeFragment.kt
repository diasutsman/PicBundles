package com.diasandfahri.picbundles.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diasandfahri.picbundles.databinding.FragmentHomeBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val viewModel: PhotoViewModel by activityViewModels()

    private val mAdapter by lazy {
        PhotoAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        if(viewModel.imagesList.value == null) viewModel.getAllPhotos(1)
        viewModel.imagesList.observe(viewLifecycleOwner) {
            Log.i("HomeFragment", "imagesList: $it")
            mAdapter.setData(it)
            binding.rvHome.adapter = mAdapter
        }

        Log.i("HomeFragment", "onCreateView")
        


        return binding.root
    }
}