package com.csd051.superiora.ui.home.today_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csd051.superiora.databinding.FragmentTodayScheduleBinding

class TodayScheduleFragment : Fragment() {

    private lateinit var todayScheduleViewModel: TodayScheduleViewModel
    private var _binding: FragmentTodayScheduleBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todayScheduleViewModel =
            ViewModelProvider(this)[TodayScheduleViewModel::class.java]

        _binding = FragmentTodayScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        todayScheduleViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}