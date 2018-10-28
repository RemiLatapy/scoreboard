package remi.scoreboard.adapter

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.joooonho.SelectableRoundedImageView

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: SelectableRoundedImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .into(view)
}