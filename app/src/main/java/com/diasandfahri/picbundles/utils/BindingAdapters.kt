package com.diasandfahri.picbundles.utils

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.diasandfahri.picbundles.R
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {
    @BindingAdapter("android:srcFromUrl")
    @JvmStatic
    fun srcFromUrl(imageView: ImageView, url: String?) {
        Log.i("BindingAdapters", "srcFromUrl: $url")
        url?.let {
            Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.ic_broken)
                .error(R.drawable.ic_broken)
                .into(imageView)
        }
    }

    @BindingAdapter("android:date")
    @JvmStatic
    fun date(tv: TextView, date: String?) {
        date?.let {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            tv.text =
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(formatter.parse(
                    date.substring(0, 10)) as Date) ?: ""
        }
    }
}