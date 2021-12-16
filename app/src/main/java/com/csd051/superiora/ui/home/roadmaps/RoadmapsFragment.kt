package com.csd051.superiora.ui.home.roadmaps

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.FragmentRoadmapsBinding
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.viewmodel.ViewModelFactory

class RoadmapsFragment : Fragment() {

    private var fragmentRoadmapsBinding: FragmentRoadmapsBinding? = null
    private val binding get() = fragmentRoadmapsBinding!!
    private lateinit var viewModel: RoadmapsViewModel
    private var currentSize : Int = 0
    private var counter : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRoadmapsBinding = FragmentRoadmapsBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[RoadmapsViewModel::class.java]
        val adapterTask = RoadmapsAdapter(viewLifecycleOwner, viewModel) {task, isDone ->
            doneTask(task, isDone)
        }
        val courseId: Int = arguments?.getInt("courseId") ?: 0


        viewModel.getAllTask().observe(viewLifecycleOwner, { data ->
            currentSize = data.size
            counter += 3
            if (counter == 5) {
                viewModel.getDataFromApi(currentSize, courseId)
            }
        })

        viewModel.getRootTask(courseId).observe(viewLifecycleOwner, { listTask ->
            if (listTask.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                adapterTask.setListTask(listTask)
            }else {
                counter += 2
                if (counter == 5) {
                    viewModel.getDataFromApi(currentSize, courseId)
                }
            }
        })

        with(binding.rvRoadmaps) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterTask
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_roadmap, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        fragmentRoadmapsBinding = null
    }
}