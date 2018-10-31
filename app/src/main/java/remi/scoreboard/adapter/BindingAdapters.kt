package remi.scoreboard.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .into(view)
}