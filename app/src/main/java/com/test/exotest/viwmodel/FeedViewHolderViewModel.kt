package com.test.exotest.viwmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.exotest.adapter.viewpager.FeedViewHolder

class FeedViewHolderViewModel : ViewModel() {
    var currentFeedViewHolder : MutableLiveData<FeedViewHolder>? = MutableLiveData()
}