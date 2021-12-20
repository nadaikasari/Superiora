package com.csd051.superiora.ui.home.today_schedule

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.FragmentTodayScheduleBinding
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.viewmodel.ViewModelFactory

class TodayScheduleFragment : Fragment() {

    private lateinit var viewModel: TodayScheduleViewModel
    private lateinit var todayScheduleAdapter: TodayScheduleAdapter
    private var _binding: FragmentTodayScheduleBinding? = null
    private var messageDataEmpty: String = ""

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodayScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[TodayScheduleViewModel::class.java]

        todayScheduleAdapter = TodayScheduleAdapter(viewLifecycleOwner, viewModel){ task, isDone ->
            doneTask(task, isDone)
        }
        getData()
        return root
    }


    private fun getData() {
        viewModel.tasks.observe(viewLifecycleOwner, { listTask ->
            if (listTask != null) {
                binding.progressBar3.visibility = View.GONE
                todayScheduleAdapter.setListTask(listTask)
            }
            binding.emptyTask.tvContentEmptyDesc.text = messageDataEmpty
            binding.emptyTask.imageView3.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
            binding.emptyTask.tvContentEmptyDesc.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
        })

        with(binding.rvTodayTask) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = todayScheduleAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_schedule, menu)

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
                    viewModel.setFilter(1)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}