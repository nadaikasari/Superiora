package com.csd051.superiora.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.ActivityDetailTaskBinding
import com.csd051.superiora.ui.edit.EditTaskActivity
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
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]

        binding.swipeRefresh.setOnRefreshListener { getData() }

        binding.rvDetailChild.layoutManager = LinearLayoutManager(this)

    }

    private fun getData() {
        task = intent.getParcelableExtra(EXTRA_DATA)

        task?.let { task ->
            binding.detailTvTitle.text = task.title
            binding.detailText.text = task.details
            val state = task.isFavorite
            setFavoriteState(state)
            if (task.triggerLink.toString().isEmpty()) {
                binding.detailVideoPlayer.visibility = View.GONE
                binding.tvTriggerLink?.visibility = View.GONE
                binding.tvTitleTriggerLink?.visibility = View.GONE
            } else {
                if (task.triggerLink.toString().contains("https://youtu.be/")) {
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

                    binding.tvTriggerLink?.visibility = View.GONE
                    binding.tvTitleTriggerLink?.visibility = View.GONE
                } else {
                    binding.tvTriggerLink?.text = task.triggerLink
                    binding.tvTriggerLink?.visibility = View.VISIBLE
                    binding.detailVideoPlayer.visibility = View.GONE
                }

            }

            viewModel.getChildTask(task.id).observe(this, { tasks ->
                if(tasks.isEmpty()) {
                    binding.nextStep.visibility = View.GONE
                } else {
                    binding.nextStep.visibility = View.VISIBLE
                    showRecyclerView(tasks)
                }
            })

            binding.btnEdittask?.visibility = View.VISIBLE
            binding.btnEdittask?.setOnClickListener {
                val intent = Intent(this, EditTaskActivity::class.java)
                intent.putExtra(EditTaskActivity.EXTRA_DATA, task)
                startActivity(intent)
            }

            if (task.details.isNullOrEmpty()) {
                binding.tvDetailTitle?.visibility = View.GONE
                binding.tvDetailsDetail?.visibility = View.GONE
            }
            binding.swipeRefresh.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun showRecyclerView(tasks: List<Task>) {
        adapter = DetailTaskAdapter(){ task ->
            val intent = Intent(this, DetailTaskActivity::class.java)
            intent.putExtra(EXTRA_DATA, task)
            startActivity(intent)
        }
        adapter.setListTask(tasks)
        binding.rvDetailChild.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_task, menu)
        this.menu = menu
        getData()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_favorite -> {
                (task?.isFavorite).let {
                    task?.isFavorite = it != true
                    if (task?.isFavorite == null) {
                        task?.isFavorite = true
                    }
                }
                viewModel.updateTask(task)
                if (task?.isFavorite == true) {
                    Toast.makeText(this, "You have favorited this task!", Toast.LENGTH_SHORT).show()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
                } else {
                    Toast.makeText(this, "You have unfavorited this task!", Toast.LENGTH_SHORT).show()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border)
                }

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

    private fun setFavoriteState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_favorite)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border)
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}