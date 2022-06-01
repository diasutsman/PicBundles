package com.diasandfahri.picbundles.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.diasandfahri.picbundles.databinding.ActivityDetailBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding as ActivityDetailBinding

    private val viewModel: PhotoViewModel by viewModels()

    private val adapter by lazy {
        PhotoAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarAsSupportActionBar()

        binding.apply {
            title = ""
            lifecycleOwner = this@DetailActivity
            photo = intent.getParcelableExtra(PHOTO_KEY)
        }

        Log.i("DetailActivity", "Photo: ${binding.photo}")

        observeImageList()
    }

    private fun observeImageList() {
        viewModel.getRelatedPhotosById(binding.photo?.id as String)
        viewModel.relatedImageList.observe(this) {
            Log.i("DetailActivity", "Photo list: $it")
            adapter.setData(it)
            binding.rvMorePicture.adapter = adapter
        }
        viewModel.currentUser.observe(this) {
            binding.photo = binding.photo?.copy(user = it)
        }
    }

    private fun setToolbarAsSupportActionBar() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val PHOTO_KEY = "PHOTO_KEY"
    }
}