package com.test.exotest.adapter.viewpager

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.test.exotest.R
import com.test.exotest.adapter.item.FeedItem

class FeedViewHolder(
        private val view : View,
        private val context : Context
) : RecyclerView.ViewHolder(view) {

    val videoPlayer : SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
    private val feedPlayerView = view.findViewById<PlayerView>(R.id.feedPlayerView)
    private val videoProgressBar = view.findViewById<ProgressBar>(R.id.feedVideoProgressBar)
    private val playImageView = view.findViewById<ImageView>(R.id.iv_play_button)
    private val feedContainer = view.findViewById<FrameLayout>(R.id.feedContainer)
    private val muteImageView = view.findViewById<ImageView>(R.id.iv_mute)

    var TAG = "FeedViewHolder"

    private var touchedVideoView : Boolean = false

    fun bind(feedItem : FeedItem) {
        initPlayerView()
        initMuteButton()
        setData(feedItem)
    }

    private fun initMuteButton() {
        muteImageView.setOnClickListener {
            if (videoPlayer.volume == 1f){
                volumOffPlayer()
                showVolumeOnButton()
            } else {
                volumeOnPlayer()
                showVolumeOffButton()
            }
        }
    }

    private fun initPlayerView() {

        feedPlayerView.useController = false
        feedPlayerView.player = videoPlayer
        feedPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        feedContainer.setOnClickListener {
            Log.d("feedviewholder", "setonclick")
            if (videoPlayer.isPlaying){
                showPlayButton()
                pausePlayer()
            } else {
                hidePlayButton()
                resumePlayer()
            }
        }

    }

    private fun setData(feedItem: FeedItem){
        Log.d(TAG, "setData")
        val videoUri = Uri.parse("https://dev-s.s3.ap-northeast-2.amazonaws.com/${feedItem.videoPath}")

        val mediaItem = MediaItem.fromUri(videoUri)

        videoPlayer.setMediaItem(mediaItem)

        videoPlayer.prepare()

        videoPlayer.addListener(object : Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    // STATE IDLE -> 재생실패
                    // STATE BUFFERING -> 재생준비
                    // STATE READY -> 재생 준비 완료
                    // STATE ENDED -> 재생 마침
                    Player.STATE_BUFFERING -> {

                    }
                    Player.STATE_READY -> {

                    }
                    Player.STATE_ENDED -> {
                        videoPlayer.seekTo(0)
                        videoPlayer.playWhenReady = true
                    }
                }
            }
        })
    }

    fun showLoading() {
        videoProgressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        videoProgressBar.visibility = View.INVISIBLE
    }

    fun showPlayButton() {
        playImageView.visibility = View.VISIBLE
    }

    fun hidePlayButton() {
        playImageView.visibility = View.INVISIBLE
    }

    fun playPlayer() {
        videoPlayer.seekTo(0)
        videoPlayer.playWhenReady = true
    }

    fun resumePlayer() {
        videoPlayer.playWhenReady = true
    }

    fun stopPlayer() {
        videoPlayer.pause()
    }

    fun pausePlayer() {
        videoPlayer.playWhenReady = false
    }

    fun getPlayer() : SimpleExoPlayer {
        return videoPlayer
    }

    fun releasePlayer() {
        videoPlayer.release()
    }

    fun volumeOnPlayer() {
        videoPlayer.volume = 1f
    }

    fun volumOffPlayer() {
        videoPlayer.volume = 0f
    }

    fun showVolumeOnButton() {
        muteImageView.setImageResource(R.drawable.ic_baseline_volume_up_24)
    }

    fun showVolumeOffButton() {
        muteImageView.setImageResource(R.drawable.ic_baseline_volume_off_24)
    }

}