package com.csd051.superiora.ui.home.yourtask

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.FragmentYourTaskBinding
import com.csd051.superiora.ui.add.AddTaskActivity
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.viewmodel.ViewModelFactory


class YourTaskFragment : Fragment() {

    private lateinit var viewModel: YourTaskViewModel
    private lateinit var adapterTask: YourTaskAdapter
    private var fragmentTaskBinding: FragmentYourTaskBinding? = null
    private val binding get() = fragmentTaskBinding!!
    private var messageDataEmpty: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTaskBinding = FragmentYourTaskBinding.inflate(inflater, container, false)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[YourTaskViewModel::class.java]
        adapterTask = YourTaskAdapter(viewLifecycleOwner, viewModel){task, isDone ->
            doneTask(task, isDone)
        }

        getData()

        binding.fab.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java)
            startActivity(intent)
        }
        return binding.root

    }

    private fun getData() {
        viewModel.tasks.observe(viewLifecycleOwner, { listTask ->
            when {
                listTask != null -> {
                    binding.progressBar3.visibility = View.GONE
                    adapterTask.setListTask(listTask)
                    setRecycler()
                }
            }
            if(messageDataEmpty == "") {
                binding.emptyTask.tvContentEmptyDesc.text = getString(R.string.notask)
            } else {
                binding.emptyTask.tvContentEmptyDesc.text = messageDataEmpty
            }
            binding.emptyTask.imageView3.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
            binding.emptyTask.tvContentEmptyDesc.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun setRecycler() {
        with(binding.rvTask) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterTask
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_your_task, menu)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.titleQuery = query
                if(query.isEmpty()){
                    viewModel.setFilter(0)
                }else{
                    viewModel.setFilter(4)
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
                if(this.isNotEmpty()){
                    for(each in this) {
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
                if(this.isNotEmpty()){
                    for(each in this) {
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
        fragmentTaskBinding = null
    }


}