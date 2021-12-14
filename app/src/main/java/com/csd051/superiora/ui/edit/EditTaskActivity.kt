package com.csd051.superiora.ui.edit

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.ActivityEditTaskBinding
import com.csd051.superiora.utils.DatePickerFragment
import com.csd051.superiora.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var recycler: RecyclerView
    private var dueDateMillis: Long = System.currentTimeMillis()
    private var task: Task? = null
    private lateinit var viewModel : EditTaskViewModel

    private lateinit var binding: ActivityEditTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.edit_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EditTaskViewModel::class.java]

        task = intent.getParcelableExtra(EXTRA_DATA)

        recycler = findViewById(R.id.rv_childtask)
        recycler.layoutManager = LinearLayoutManager(this)

        task?.let { task ->
            binding.addEdTitle.setText(task.title)
            binding.addTvDueDate.text = task.dueDate
            binding.addEdTriggerlink.setText(task.triggerLink)
            binding.addEdDescription.setText(task.details)
        }

        binding.btnSave.setOnClickListener {
            task.let {
                task?.title = binding.addEdTitle.text.toString()
                task?.dueDate = binding.addTvDueDate.text.toString()
                task?.triggerLink = binding.addEdTriggerlink.text.toString()
                task?.details = binding.addEdDescription.text.toString()
            }
            task?.let { it -> viewModel.updateTask(it) }
            finish()
        }

        binding.addChild.setOnClickListener {
            val childTask = Task()
            childTask.let {
                childTask.id_firebase = ""
                childTask.id_parent = task?.id ?: -1
                childTask.title = binding.edtNewChildName.text.toString()
            }
            viewModel.insertChild(childTask)
            binding.edtNewChildName.setText("")
        }

        viewModel.getChildTask(task?.id ?: -2).observe(this, { tasks ->
            showRecyclerView(tasks)
        })

    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

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

    private fun showRecyclerView(tasks: List<Task>) {
        val adapter = EditTaskAdapter { task ->
            viewModel.deleteChild(task)
        }

        adapter.setListTask(tasks)
        recycler.adapter = adapter
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
