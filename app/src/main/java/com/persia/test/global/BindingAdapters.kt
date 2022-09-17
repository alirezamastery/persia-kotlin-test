package com.persia.test.global

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.persia.test.R

/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
// @BindingAdapter("listData")
// fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?) {
//     val adapter = recyclerView.adapter as PhotoGridAdapter
//     adapter.submitList(data)
// }

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .fitCenter()
            .circleCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgView)
    }
}

/**
 * This binding adapter displays the [RequestState] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("requestState")
fun bindStatus(statusImageView: ImageView, requestState: Constants.RequestState?) {
    when (requestState) {
        Constants.RequestState.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        Constants.RequestState.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}
