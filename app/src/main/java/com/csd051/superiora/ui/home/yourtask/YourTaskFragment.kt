package com.csd051.superiora.ui.home.yourtask

import android.content.Intent
import android.os.Bundle
import android.view.*
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

    private var fragmentTaskBinding: FragmentYourTaskBinding? = null
    private val binding get() = fragmentTaskBinding!!
    private lateinit var viewModel: YourTaskViewModel
    private lateinit var adapterTask: YourTaskAdapter


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
        viewModel.tasks.observe(viewLifecycleOwner, { listTask ->
            if (listTask != null) {
                binding.progressBar3.visibility = View.GONE
                adapterTask.setListTask(listTask)
                binding.emptyTask.imageView3.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
                binding.emptyTask.tvContentEmptyDesc.visibility = if (listTask.isEmpty()) View.VISIBLE else View.GONE
                setRecycler()
            }
        })

        binding.fab.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java)
            startActivity(intent)
        }
        return binding.root

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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.allTask -> {
                viewModel.setFilter(0)
                true
            }
            R.id.is_active -> {
                viewModel.setFilter(1)
                true
            }
            R.id.complete -> {
                viewModel.setFilter(2)
                true
            }
            R.id.favorite -> {
                viewModel.setFilter(3)
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