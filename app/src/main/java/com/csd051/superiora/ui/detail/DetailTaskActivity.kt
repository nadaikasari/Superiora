package com.csd051.superiora.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.ActivityDetailTaskBinding
import com.csd051.superiora.viewmodel.ViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding
    private var task: Task? = null
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var viewModel: DetailTaskViewModel
    private lateinit var adapter: DetailTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]

        binding.swipeRefresh.setOnRefreshListener { getData() }

        getData()

        binding.rvDetailChild.layoutManager = LinearLayoutManager(this)

    }

    private fun getData() {
        task = intent.getParcelableExtra(EXTRA_DATA)

        task?.let { task ->
            binding.detailTvTitle.text = task.title
            binding.detailText.text = task.details

            if (task.triggerLink.toString().isEmpty()) {
                binding.detailVideoPlayer.visibility = View.GONE
            } else {
                youTubePlayerView = binding.detailVideoPlayer
                lifecycle.addObserver(youTubePlayerView)
                val videoId = task.triggerLink.toString().replace("https://youtu.be/", "")

                youTubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)

                    }

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                        youTubePlayer.loadVideo(videoId, 0f)
                        super.onError(youTubePlayer, error)
                    }
                })
            }

            viewModel.getChildTask(task.id).observe(this, { tasks ->
                if(tasks.isEmpty()) {
                    binding.nextStep.visibility = View.GONE
                } else {
                    binding.nextStep.visibility = View.VISIBLE
                    showRecyclerView(tasks)
                }
            })

            binding.swipeRefresh.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun showRecyclerView(tasks: List<Task>) {
        adapter = DetailTaskAdapter()
        adapter.setListTask(tasks)
        binding.rvDetailChild.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_favorite -> {
                Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Learn " + binding.detailTvTitle.text + " in Superiora App " +
                            task?.let { task -> task.triggerLink.toString() }
                )
                sendIntent.type = "text/plain"

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}