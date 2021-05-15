package com.test.exotest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.test.exotest.R
import com.test.exotest.adapter.item.FeedItem
import com.test.exotest.adapter.viewpager.FeedViewHolder

class FeedsAdapter(
    private val feedItems : ArrayList<FeedItem>,
    private val context : Context
) : RecyclerView.Adapter<FeedViewHolder>() {

    val holderList : HashMap<Int, FeedViewHolder> = HashMap()

    fun getItem(position: Int) : FeedItem {
        return feedItems[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed, parent, false), context)
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(feedItems[position])

        if (holderList.containsKey(position).not()){
            holderList[position] = holder
        }
    }

    override fun getItemCount(): Int {
        return feedItems.size
    }

    fun getHolderByPosition(position: Int) : FeedViewHolder? {
        return holderList[position]
    }
}