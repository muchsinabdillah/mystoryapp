package com.example.mystoryapp.ui.createstory

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.databinding.ItemstoryBinding
import com.example.mystoryapp.ui.detailstory.DetailHistoryActivity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private var addressName: String? = null

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {


    //    private var liststory: MutableList<StoryEntity> = mutableListOf()
//
//    fun setStory(story: List<StoryEntity>) {
//        liststory.clear()
//        liststory.addAll(story)
//        notifyDataSetChanged()
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemstory = ItemstoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoryViewHolder(itemstory)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        val course = liststory[position]
//        holder.bind(course)
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }

    class StoryViewHolder(private val binding: ItemstoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryEntity) {
            with(binding) {
                val instant = Instant.parse(story.createdAt)
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
                    .withZone(ZoneId.of(TimeZone.getDefault().id))

                if (story.lat.toString().isBlank() and story.lon.toString().isBlank()) {
                    txtLocation.text = ""
                } else {

                    val geocoder = Geocoder(itemView.context, Locale.getDefault())
                    val lat = story.lat.toString()
                    val lon = story.lon.toString()
                    if (story.lat.equals(null) and story.lon.equals(null)) {
                        txtLocation.text = ""
                    } else {
                        Log.d("datasaya", "0 {$lat = $lon}")
                        val list = geocoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
                        if (list != null && list.size != 0) {
                            addressName = list[0].subAdminArea
                            txtLocation.text = addressName
                        } else {
                            txtLocation.text = ""
                        }
                    }

                }

                tvItemName.text = story.name
                ivItemDate.text = formatter.format(instant)

                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_lock_24)
                            .error(R.drawable.ic_baseline_lock_24)
                    )
                    .into(binding.ivItemPhoto)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailHistoryActivity::class.java).apply {
                        putExtra(DetailHistoryActivity.ID, story.id)
                        putExtra("id", story.id)
                        putExtra("name", story.name)
                        putExtra("description", story.description)
                        putExtra("createdAt", formatter.format(instant))
                        putExtra("photoUrl", story.photoUrl)
                        putExtra("addressName", addressName)
                    }
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(ivItemPhoto, "aminmasi_detailptouser"),
                            androidx.core.util.Pair(tvItemName, "aminmasi_namauser"),
                            androidx.core.util.Pair(ivItemDate, "aminmasi_date"),
                            androidx.core.util.Pair(ivItemPhoto, "aminmasi_image")
                        )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
          val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
//    private fun getAddressName(lat: Double, lon: Double): String? {
//        var addressName: String? = null
//        val geocoder = Geocoder(context, Locale.getDefault())
//        try {
//            val list = geocoder.getFromLocation(lat, lon, 1)
//            if (list != null && list.size != 0) {
//                addressName = list[0].getAddressLine(0)
//
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return addressName
//    }

}