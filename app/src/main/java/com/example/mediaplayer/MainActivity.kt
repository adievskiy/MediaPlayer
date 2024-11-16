package com.example.mediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var songList = mutableListOf(R.raw.sinatra, R.raw.malenkojelochke, R.raw.jinglebells)
    private var songIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {
        binding.prevBTN.setOnClickListener {
            previousSong()
        }

        binding.nextBTN.setOnClickListener {
            nextSong()
        }

        binding.pauseBTN.setOnClickListener {
            mediaPlayer?.pause()
        }

        binding.playBTN.setOnClickListener {
            playCurrentSong()
        }

        binding.stopBTN.setOnClickListener {
            stopCurrentSong()
        }

        binding.seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun playCurrentSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songList[songIndex])
            initializeSeekbar()
        }
        mediaPlayer?.start()
    }

    private fun stopCurrentSong() {
        mediaPlayer?.let {
            it.stop()
            it.reset()
            it.release()
            mediaPlayer = null
        }
    }

    private fun previousSong() {
        if (songIndex > 0) {
            songIndex--
        } else {
            songIndex = songList.size - 1
        }
        stopCurrentSong()
        playCurrentSong()
    }

    private fun nextSong() {
        if (songIndex < songList.size - 1) {
            songIndex++
        } else {
            songIndex = 0
        }
        stopCurrentSong()
        playCurrentSong()
    }

    private fun initializeSeekbar() {
        binding.seekbar.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekbar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekbar.progress = 0
                }
            }
        }, 0)
    }
}