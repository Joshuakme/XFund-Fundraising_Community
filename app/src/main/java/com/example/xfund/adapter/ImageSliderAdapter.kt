package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.xfund.R
import com.example.xfund.model.NewsModel


class ImageSliderAdapter(private val context: Context, private var newsList: List<NewsModel>) : PagerAdapter() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    override fun getCount(): Int {
        return newsList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.news_image_slider_item, container, false)

        val newsItem = newsList[position]

        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textTitle: TextView = view.findViewById(R.id.textTitle)

        view.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }


        // Load image using Glide
        Glide.with(context)
            .load(newsItem.imageUrl)
            .into(imageView)

        textTitle.text = newsItem.title

        container.addView(view)
        return view
    }

    fun updateData(newData: List<NewsModel>) {
        newsList = newData
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}