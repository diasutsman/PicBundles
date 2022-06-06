package com.diasandfahri.picbundles.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
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

    private var _photo: PhotoItem? = null
    private val photo get() = _photo as PhotoItem

    private val adapter by lazy {
        PhotoAdapter(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        _photo = intent.getParcelableExtra(PHOTO_KEY)
        setContentView(binding.root)
        setToolbarAsSupportActionBar()

        setupBindings()

        observeImageList()

        implementBookMarkButton()

        setupDownloadButton()
    }

    private fun setupDownloadButton() {
        binding.btnDownload.apply {
            setOnClickListener {
                Log.i("Photo", "Downloading $photo")
                viewModel.downloadPhoto(context, photo)
            }
        }
    }

    private fun setupBindings() {
        binding.apply {
            rvMorePicture.adapter = adapter
            title = ""
            lifecycleOwner = this@DetailActivity
            mPhoto = photo
        }
    }

    private fun implementBookMarkButton() {
        binding.btnBookmark.apply {
            viewModel.isBookmarked(photo)
                .observe(this@DetailActivity) { isBookmarked ->
                    icon = AppCompatResources.getDrawable(this@DetailActivity,
                        if (isBookmarked) R.drawable.ic_bookmark_filled
                        else R.drawable.ic_bookmark
                    )
                    var message: String
                    setOnClickListener {
                        message = if (isBookmarked) {
                            viewModel.unBookmarkPhoto(photo)
                            context.getString(R.string.txt_bookmark_removed)
                        } else {
                            viewModel.bookmarkPhoto(photo)
                            context.getString(R.string.txt_bookmark_added)
                        }
                        Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun observeImageList() {
        viewModel.apply {
            getRelatedPhotosById(photo.id as String)
            relatedImageList.observe(this@DetailActivity) {
                adapter.setData(it)
            }
            isRelatedLoading.observe(this@DetailActivity) {
                showLoading(it)
            }
            currentRelatedResponse.observe(this@DetailActivity) {
                binding.mPhoto = photo.copy(user = it?.user, links = it?.links)
                if(photo.links == null) _photo = photo.copy(links = it?.links)
                viewModel.saveUserIfNotExist(binding.mPhoto as PhotoItem)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
                rvMorePicture.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                rvMorePicture.visibility = View.VISIBLE
            }
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