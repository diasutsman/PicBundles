package com.diasandfahri.picbundles.ui.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.diasandfahri.picbundles.R
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.databinding.ActivityDetailBinding
import com.diasandfahri.picbundles.ui.PhotoAdapter
import com.diasandfahri.picbundles.ui.PhotoViewModel

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding as ActivityDetailBinding

    private val viewModel: PhotoViewModel by viewModels()

    private val adapter by lazy {
        PhotoAdapter(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarAsSupportActionBar()

        setupBindings()

        observeImageList()

        implementBookMarkButton()
    }

    private fun setupBindings() {
        binding.apply {
            rvMorePicture.adapter = adapter
            title = ""
            lifecycleOwner = this@DetailActivity
            photo = intent.getParcelableExtra(PHOTO_KEY)
        }
    }

    private fun implementBookMarkButton() {
        binding.btnBookmark.apply {
            viewModel.isBookmarked(binding.photo as PhotoItem)
                .observe(this@DetailActivity) { isBookmarked ->
                    icon = AppCompatResources.getDrawable(this@DetailActivity,
                        if (isBookmarked) R.drawable.ic_bookmark_filled
                        else R.drawable.ic_bookmark
                    )
                    var message: String
                    setOnClickListener {
                        message = if (isBookmarked) {
                            viewModel.unBookmarkPhoto(binding.photo as PhotoItem)
                            context.getString(R.string.txt_bookmark_removed)
                        } else {
                            viewModel.bookmarkPhoto(binding.photo as PhotoItem)
                            context.getString(R.string.txt_bookmark_added)
                        }
                        Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun observeImageList() {
        viewModel.getRelatedPhotosById(binding.photo?.id as String)
        viewModel.relatedImageList.observe(this) {
            adapter.setData(it)
        }
        viewModel.currentUser.observe(this) {
            binding.photo = binding.photo?.copy(user = it)
            viewModel.saveUserIfNotExist(binding.photo as PhotoItem)
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