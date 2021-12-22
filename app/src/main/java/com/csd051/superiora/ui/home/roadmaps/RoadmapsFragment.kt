package com.csd051.superiora.ui.home.roadmaps

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.FragmentRoadmapsBinding
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.viewmodel.ViewModelFactory
import com.csd051.superiora.vo.Status

class RoadmapsFragment : Fragment() {

    private var fragmentRoadmapsBinding: FragmentRoadmapsBinding? = null
    private val binding get() = fragmentRoadmapsBinding!!
    private lateinit var viewModel: RoadmapsViewModel
    private var currentSize: Int = 0
    private var messageDataEmpty: String = ""
    private lateinit var adapterTask : RoadmapsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRoadmapsBinding = FragmentRoadmapsBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[RoadmapsViewModel::class.java]
        adapterTask = RoadmapsAdapter(resources,viewLifecycleOwner, viewModel) { task, isDone ->
            doneTask(task, isDone)
        }
        val courseId: Int = arguments?.getInt("courseId") ?: 0

        viewModel.setCourseId(courseId)

        viewModel.getAllTask().observe(viewLifecycleOwner, { data ->
            currentSize = data.size
            getData(currentSize, courseId)

        })

        viewModel.tasks.observe(viewLifecycleOwner, { listTask ->
            if (listTask.isNotEmpty()) {
                binding.rvRoadmaps.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                adapterTask.setListTask(listTask)
                setRecycler()
            } else {
                binding.rvRoadmaps.visibility = View.GONE
            }
            setLayout()
        })

        return binding.root
    }

    private fun getData(tableLength: Int, courseId: Int) {
        viewModel.getDataFromApi(tableLength, courseId).observe(viewLifecycleOwner, { listMovies ->
            if (listMovies != null) {
                when (listMovies.status) {
                    Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
    private fun setRecycler() {
        with(binding.rvRoadmaps) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterTask
        }
    }

    private fun setLayout() {
        viewModel.tasks.observe(viewLifecycleOwner, { listTask ->
            if (messageDataEmpty == "") {
                binding.emptyTask.tvContentEmptyDesc.text = getString(R.string.notask)
            } else {
                binding.emptyTask.tvContentEmptyDesc.text = messageDataEmpty
            }
            binding.emptyTask.imageView3.visibility =
                if (listTask.isEmpty()) View.VISIBLE else View.GONE
            binding.emptyTask.tvContentEmptyDesc.visibility =
                if (listTask.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_roadmap, menu)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.titleQuery = query
                if (query.isEmpty()) {
                    viewModel.setFilter(0)
                } else {
                    viewModel.setFilter(4)
                    messageDataEmpty = getString(R.string.noresult_search)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener {
            viewModel.setFilter(0)
            false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.allTask -> {
                viewModel.setFilter(0)
                messageDataEmpty = getString(R.string.notask)
                true
            }
            R.id.is_active -> {
                viewModel.setFilter(1)
                messageDataEmpty = getString(R.string.noactive_data)
                true
            }
            R.id.complete -> {
                viewModel.setFilter(2)
                messageDataEmpty = getString(R.string.nocomplete_task)
                true
            }
            R.id.favorite -> {
                viewModel.setFilter(3)
                messageDataEmpty = getString(R.string.no_favorite_task)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doneTask(task: Task, isDone: Boolean) {
        AppExecutors().diskIO().execute {
            with(viewModel.getStaticChild(task.id)) {
                if (this.isNotEmpty()) {
                    for (each in this) {
                        doneByParent(each, isDone)
                    }
                }
            }
        }

        task.isDone = isDone
        viewModel.updateTask(task)
    }

    private fun doneByParent(task: Task, isDone: Boolean) {
        AppExecutors().diskIO().execute {
            with(viewModel.getStaticChild(task.id)) {
                if (this.isNotEmpty()) {
                    for (each in this) {
                        doneByParent(each, isDone)
                    }
                }
            }
        }
        task.isDoneByParent = isDone
        viewModel.updateTask(task)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentRoadmapsBinding = null
    }
}