package com.diasandfahri.picbundles.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.databinding.FragmentHomeBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val viewModel: PhotoViewModel by activityViewModels()

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = true
        // call api to reload the screen
        viewModel.getAllPhotos()
    }

    private val mAdapter by lazy {
        PhotoAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupBindings()
        setRefreshLayout()
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvHome.apply {
            adapter = mAdapter
        }
    }

    private fun setupBindings() {
        if (viewModel.imagesList.value == null) viewModel.getAllPhotos()
        viewModel.imagesList.observe(viewLifecycleOwner) { showData(it) }
        viewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        viewModel.isError.observe(viewLifecycleOwner) { showError(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvHome.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun showData(data: List<PhotoItem>?) {
        binding.swipeRefreshLayout.isRefreshing = false
        mAdapter.setData(data)
    }

    private fun showError(error: Throwable?) {
        binding.swipeRefreshLayout.isRefreshing = false
        error?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)
    }

    // prevent memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}