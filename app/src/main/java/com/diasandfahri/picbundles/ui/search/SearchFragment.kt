package com.diasandfahri.picbundles.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.diasandfahri.picbundles.R
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.databinding.FragmentSearchBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel
import com.google.android.material.button.MaterialButton

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding as FragmentSearchBinding

    private val viewModel: PhotoViewModel by viewModels()

    private val adapter by lazy {
        PhotoAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        makeFlexButtonsGroup()

        binding.rvSearch.adapter = adapter

        setupSearchView()

        observeVariables()

        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.apply {
                        flSearch.visibility = View.GONE
                        tvTitleSearch.visibility = View.GONE
                    }
                    if (query.isNotEmpty()) viewModel.searchPhotoByQuery(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (query.isEmpty()) {
                        binding.apply {
                            viewModel.searchList.value = null
                        }
                    }
                }
                return true
            }

        })
    }

    private fun observeVariables() {
        viewModel.searchList.observe(viewLifecycleOwner) { showData(it) }
        viewModel.isSearchLoading.observe(viewLifecycleOwner) { showLoading(it) }
        viewModel.isSearchError.observe(viewLifecycleOwner) { showError(it) }
    }

    private fun showData(data: List<PhotoItem>?) {
        binding.apply {
            if (data == null) {
                tvTitleSearch.visibility = View.VISIBLE
                flSearch.visibility = View.VISIBLE
                rvSearch.visibility = View.GONE
            } else {
                flSearch.visibility = View.GONE
                tvTitleSearch.visibility = View.GONE
            }
        }
        Log.i("SearchFragment", "showData: $data")
        adapter.setData(data)
    }

    private fun showError(error: Throwable?) {
        error?.let {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean?) {
        isLoading?.let {
            binding.apply {
                pbLoading.visibility = if (it) View.VISIBLE else View.GONE
                rvSearch.visibility = if (it) View.GONE else View.VISIBLE
                if(it)adapter.setData(listOf())
            }
        }
    }

    private fun makeFlexButtonsGroup() {
        binding.flSearch.apply {
            removeAllViews()
            resources.getStringArray(R.array.searches).forEach { txtSearch ->
                val button = LayoutInflater.from(context).inflate(R.layout.btn_searches,
                    LinearLayout(this@SearchFragment.context as Context),
                    false) as MaterialButton
                button.apply {
                    parent?.let { (it as ViewGroup).removeView(button) }
                    text = txtSearch
                    setOnClickListener {
                        binding.searchView.setQuery(txtSearch, true)
                    }
                }
                addView(button)
            }
        }
    }

    // prevent memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}