package com.csd051.superiora.ui.home.yourtask

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.csd051.superiora.R
import com.csd051.superiora.databinding.FragmentYourTaskBinding
import com.csd051.superiora.ui.add.AddTaskActivity


class YourTaskFragment : Fragment() {

    private var fragmentTaskBinding: FragmentYourTaskBinding? = null
    private val binding get() = fragmentTaskBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTaskBinding = FragmentYourTaskBinding.inflate(inflater, container, false)
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