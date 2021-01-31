package com.udacity.asteroidradar

import android.accounts.AuthenticatorDescription
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("listDescription")
fun bindAsteroidDescription(view: View, description: String) {
    view.contentDescription = String.format(
        view.resources.getString(R.string.asteroid_list_content_description_format),
        description
    )
}

@BindingAdapter("imageOfTheDay")
fun bindImageOfTheDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        Picasso.with(imageView.context).load(it.url).into(imageView)
        imageView.contentDescription = String.format(
            imageView.context.getString(R.string.nasa_picture_of_day_content_description_format),
            pictureOfDay.title
        )
    }
}



@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)

    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.contentDescription =
        textView.context.getString(R.string.absolute_magnitude_title) + " is " + textView.text
}

@BindingAdapter("astronomicalUnitTextDistance")
fun bindTextViewToAstronomicalUnitDistance(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.contentDescription =
        textView.context.getString(R.string.distance_from_earth_title) + " is " + textView.text
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
    textView.contentDescription =
        textView.context.getString(R.string.estimated_diameter_title) + " is " + textView.text
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.contentDescription =
        textView.context.getString(R.string.relative_velocity_title) + " is " + textView.text
}


@BindingAdapter("closeApproachDateText")
fun bindTextViewTocloseApproachDate(textView: TextView, text: String) {
    textView.text = text
    textView.contentDescription =
        textView.context.getString(R.string.close_approach_data_title) + " is " + text
}