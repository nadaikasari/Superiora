package com.csd051.superiora.ui.add

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.ActivityAddTaskBinding
import com.csd051.superiora.utils.DatePickerFragment
import com.csd051.superiora.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var taskViewModel: AddTaskViewModel

    private var dueDateMillis: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.addTask)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        taskViewModel = ViewModelProvider(this, factory)[AddTaskViewModel::class.java]

        binding.btnSave.setOnClickListener {
            validate()
        }
    }

    private fun validate() {
        if(binding.addEdTitle.text.toString().isNotEmpty()) {
            insertTask()
        } else {
            binding.addEdTitle.error = getString(R.string.tv_field_notnull)
            binding.addEdTitle.requestFocus()
        }
    }

    private fun insertTask() {
        val task = Task()
        task.let {
            task.id_firebase = ""
            task.id_parent = -1
            task.title = binding.addEdTitle.text.toString()
            if(binding.addTvDueDate.text.equals("Due Date")) {
                task.dueDate = ""
            } else {
                task.dueDate = binding.addTvDueDate.text.toString()
            }
            task.triggerLink = binding.addEdTriggerlink.text.toString()
            task.details = binding.addEdDescription.text.toString()
        }
        taskViewModel.insert(task)
        Toast.makeText(this, getString(R.string.added), Toast.LENGTH_SHORT).show()
        finish()
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.addTvDueDate.text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}