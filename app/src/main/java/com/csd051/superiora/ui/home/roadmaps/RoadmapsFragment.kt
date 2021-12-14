package com.csd051.superiora.ui.home.roadmaps

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.databinding.FragmentRoadmapsBinding
import com.csd051.superiora.databinding.FragmentYourTaskBinding
import com.csd051.superiora.ui.add.AddTaskActivity
import com.csd051.superiora.ui.home.yourtask.YourTaskAdapter
import com.csd051.superiora.ui.home.yourtask.YourTaskViewModel
import com.csd051.superiora.viewmodel.ViewModelFactory

class RoadmapsFragment : Fragment() {

    private var fragmentRoadmapsBinding: FragmentYourTaskBinding? = null
    private val binding get() = fragmentRoadmapsBinding!!
    private lateinit var viewModel: RoadmapsViewModel
    private var currentSize : Int = 0
    private var counter : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRoadmapsBinding = FragmentYourTaskBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[RoadmapsViewModel::class.java]
        val adapterTask = RoadmapsAdapter(viewLifecycleOwner, viewModel)
        val courseId: Int = arguments?.getInt("courseId") ?: 0
        // TODO CourseIdnya kudu di load dlu pake EXTRA
        viewModel.getAllTask().observe(viewLifecycleOwner, { data ->
            currentSize = data.size
            counter += 3
            if (counter == 5) {
                viewModel.getDataFromApi(currentSize, courseId)
            }
        })

        viewModel.getRootTask(courseId).observe(viewLifecycleOwner, { listTask ->
            if (listTask.isNotEmpty()) {
                binding.progressBar3.visibility = View.GONE
                adapterTask.setListTask(listTask)
            }else {
                counter += 2
                if (counter == 5) {
                    viewModel.getDataFromApi(currentSize, courseId)
                }
            }
        })

        with(binding.rvTask) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterTask
        }

        binding.fab.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java)
            startActivity(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentRoadmapsBinding = null
    }
}