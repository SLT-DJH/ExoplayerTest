package com.test.exotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.test.exotest.adapter.FeedsAdapter
import com.test.exotest.adapter.item.FeedItem
import com.test.exotest.adapter.viewpager.FeedViewHolder
import com.test.exotest.viwmodel.FeedViewHolderViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var feedsAdapter : FeedsAdapter
    private lateinit var viewPager : ViewPager2
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var currentFeedViewHolderViewModel: FeedViewHolderViewModel

    private var currentInit = false

    private var currentItemSum : Int? = null

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentFeedViewHolderViewModel = ViewModelProvider(this)
            .get(FeedViewHolderViewModel::class.java)

        viewPager = findViewById(R.id.viewpager2_feed)
        swipeRefresh = findViewById(R.id.swipe_refresh)

        if (!currentInit) {
            initViewPager()
            currentInit = true
        }

        swipeRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                releaseAllPlayer()
                initViewPager()
                swipeRefresh.isRefreshing = false
            }
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "viewpageron selected page : $position, ${feedsAdapter.getItem(position).videoPath}" +
                        "${feedsAdapter.getHolderByPosition(position)!!.videoPlayer}")

                currentFeedViewHolderViewModel.currentFeedViewHolder?.value =
                    feedsAdapter.getHolderByPosition(position)

                Log.d(TAG, "current : ${currentFeedViewHolderViewModel.currentFeedViewHolder?.value}")

                currentFeedViewHolderViewModel.currentFeedViewHolder?.value?.playPlayer()

                if (position == 0){
                    nextPlayerSetting(position)
                } else if (position == feedsAdapter.itemCount) {
                    previousPlayerSetting(position)
                } else {
                    nextPlayerSetting(position)
                    previousPlayerSetting(position)
                }
            }
        })

    }

    private fun initViewPager() {

        feedsAdapter = FeedsAdapter(setData(), applicationContext)

        viewPager.adapter = feedsAdapter
        viewPager.offscreenPageLimit = 1

    }

    private fun setData() : ArrayList<FeedItem> {
        Log.d(TAG, "setData(), feedItem added")

        val feedItemList : ArrayList<FeedItem> = ArrayList()

        feedItemList.add(FeedItem("4dcc941cbfc8385c5c02de9b5c77fd0d.mp4"))
        feedItemList.add(FeedItem("a8af3d92a758bdef70a3d02a82ca4aa9.mp4"))
        feedItemList.add(FeedItem("917f68fb6f362d3a0bf8cbed5e517313.mp4"))
        feedItemList.add(FeedItem("6b052c41c6d531e8567c2b2e4e0a6de1.mp4"))
        feedItemList.add(FeedItem("7279d1980fa92902b11b92ab8c9a7a9e.mp4"))
        feedItemList.add(FeedItem("a746c32caeba1402db4c4ddf0cfe035c.mp4"))
        feedItemList.add(FeedItem("0322ea079a810aa9c92ecda86ca38638.mp4"))
        feedItemList.add(FeedItem("6f5211125ca2e8e1fcc165ba6587f4f9.mp4"))

        return feedItemList
    }

    private fun nextPlayerSetting(currentPosition : Int) {
        val nextPosition = currentPosition + 1

        feedsAdapter.getHolderByPosition(nextPosition)?.stopPlayer()
        feedsAdapter.getHolderByPosition(nextPosition)?.volumeOnPlayer()
        feedsAdapter.getHolderByPosition(nextPosition)?.hidePlayButton()
    }

    private fun previousPlayerSetting(currentPosition : Int) {
        val previousPosition = currentPosition - 1

        feedsAdapter.getHolderByPosition(previousPosition)?.stopPlayer()
        feedsAdapter.getHolderByPosition(previousPosition)?.volumeOnPlayer()
        feedsAdapter.getHolderByPosition(previousPosition)?.hidePlayButton()
    }

    override fun onPause() {
        super.onPause()
        val currentViewHolder = currentFeedViewHolderViewModel.currentFeedViewHolder?.value
        currentViewHolder?.pausePlayer()

        if (currentViewHolder?.videoPlayer?.volume == 0f) {
            currentViewHolder.showVolumeOnButton()
        } else {
            currentViewHolder?.showVolumeOffButton()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentViewHolder = currentFeedViewHolderViewModel.currentFeedViewHolder?.value
        currentViewHolder?.resumePlayer()

        if (currentViewHolder?.videoPlayer?.volume == 0f) {
            currentViewHolder.showVolumeOnButton()
        } else {
            currentViewHolder?.showVolumeOffButton()
        }

        currentViewHolder?.hidePlayButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseAllPlayer()
    }

    private fun releaseAllPlayer() {
        for(feedViewHolder in feedsAdapter.holderList.values){
            feedViewHolder.releasePlayer()
        }
    }
}