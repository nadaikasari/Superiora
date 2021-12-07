package com.csd051.superiora.ui.home.roadmaps

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.csd051.superiora.R
import com.csd051.superiora.databinding.FragmentRoadmapsBinding

class RoadmapsFragment : Fragment() {

    private var fragmentRoadmapsBinding: FragmentRoadmapsBinding? = null
    private val binding get() = fragmentRoadmapsBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRoadmapsBinding = FragmentRoadmapsBinding.inflate(inflater, container, false)
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