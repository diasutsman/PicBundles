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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("SearchFragment", "onQueryTextChange: $newText")
                newText?.let {
                    binding.apply {
                        flSearch.visibility = View.GONE
                        if (newText.isEmpty()) {
                            tvTitleSearch.visibility = View.VISIBLE
                            flSearch.visibility = View.VISIBLE
                            pbLoading.visibility = View.GONE
                            rvSearch.visibility = View.GONE
                        } else {
                            tvTitleSearch.visibility = View.GONE
                            flSearch.visibility = View.GONE
                        }
                    }
                    if(newText.isNotEmpty())viewModel.searchPhotoByQuery(newText)
                }
                return true
            }

        })

        viewModel.searchList.observe(viewLifecycleOwner) {
            Log.i("SearchFragment", "searchList: $it")
            adapter.setData(it)
        }
        viewModel.isSearchLoading.observe(viewLifecycleOwner) { showLoading(it) }
        viewModel.isSearchError.observe(viewLifecycleOwner) { showError(it) }

        return binding.root
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
//                if (viewModel.searchList.value == null) flSearch.visibility =
//                    if (it) View.GONE else View.VISIBLE
                rvSearch.visibility = if (it) View.GONE else View.VISIBLE
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
                button.parent?.let {
                    (it as ViewGroup).removeView(button)
                }
                button.text = txtSearch
                addView(button)
            }
        }
    }

}