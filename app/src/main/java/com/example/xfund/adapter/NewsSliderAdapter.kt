package com.example.xfund.adapter


import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.xfund.R
import com.example.xfund.model.NewsSliderItem
import com.google.android.material.imageview.ShapeableImageView


class NewsSliderAdapter (
    private val context: Context,
    private val newsArrayList: ArrayList<NewsSliderItem>,
    private val viewPager2: ViewPager2) :RecyclerView.Adapter<NewsSliderAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ShapeableImageView = itemView.findViewById(R.id.newsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_news_list_item, parent, false)

        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(position)
        if(position == newsArrayList.size-1) {
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        newsArrayList.addAll(newsArrayList)
        notifyDataSetChanged()
    }
}