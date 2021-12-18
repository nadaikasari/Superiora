package com.csd051.superiora.ui.home.today_schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.FragmentTodayScheduleBinding
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.viewmodel.ViewModelFactory

class TodayScheduleFragment : Fragment() {

    private lateinit var viewModel: TodayScheduleViewModel
    private lateinit var todayScheduleAdapter: TodayScheduleAdapter
    private var _binding: FragmentTodayScheduleBinding? = null


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
        viewModel.getTodayTask().observe(viewLifecycleOwner, { listTask ->
            if (listTask != null) {
                binding.progressBar3.visibility = View.GONE
                todayScheduleAdapter.setListTask(listTask)
                binding.emptyTask.imageView3.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
                binding.emptyTask.tvContentEmptyDesc.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE

                Log.d( "getData: ", listTask.toString())
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}