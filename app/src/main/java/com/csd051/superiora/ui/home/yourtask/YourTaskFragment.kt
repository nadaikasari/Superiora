package com.csd051.superiora.ui.home.yourtask

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.databinding.FragmentYourTaskBinding
import com.csd051.superiora.ui.add.AddTaskActivity
import com.csd051.superiora.viewmodel.ViewModelFactory


class YourTaskFragment : Fragment() {

    private var fragmentTaskBinding: FragmentYourTaskBinding? = null
    private val binding get() = fragmentTaskBinding!!
    private lateinit var viewModel: YourTaskViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTaskBinding = FragmentYourTaskBinding.inflate(inflater, container, false)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[YourTaskViewModel::class.java]
        val adapterTask = YourTaskAdapter()

        viewModel.getAllTask().observe(viewLifecycleOwner, { listTask ->
            if (listTask != null) {
                binding.progressBar3.visibility = View.GONE
                adapterTask.setListTask(listTask)
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
        inflater.inflate(R.menu.menu_your_task, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTaskBinding = null
    }


}