package com.csd051.superiora.ui.edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
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

        binding.rvChildtask.layoutManager = LinearLayoutManager(this)

        task?.let { task ->
            binding.addEdTitle.setText(task.title)
            binding.addTvDueDate.text = task.dueDate
            binding.addEdTriggerlink.setText(task.triggerLink)
            binding.addEdDescription.setText(task.details)
        }

        binding.btnSave.setOnClickListener {
            if(binding.addEdTitle.text.toString().isNotEmpty()) {
                task.let {
                    task?.title = binding.addEdTitle.text.toString()
                    task?.dueDate = binding.addTvDueDate.text.toString()
                    task?.triggerLink = binding.addEdTriggerlink.text.toString()
                    task?.details = binding.addEdDescription.text.toString()
                }
                task?.let { task -> viewModel.updateTask(task) }
                Toast.makeText(this, R.string.task_updated, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.addEdTitle.error = getString(R.string.tv_field_notnull)
                binding.addEdTitle.requestFocus()
            }

        }

        binding.addChild.setOnClickListener {
            if(binding.edtNewChildName.text.isNotEmpty()) {
                val childTask = Task()
                childTask.let {
                    childTask.id_firebase = ""
                    childTask.id_parent = task?.id ?: -1
                    childTask.title = binding.edtNewChildName.text.toString()
                }
                viewModel.insertChild(childTask)
                binding.edtNewChildName.setText("")
            }
            else {
                binding.edtNewChildName.error = getString(R.string.tv_field_notnull)
                binding.edtNewChildName.requestFocus()

            }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.delete_task -> {
                showDialog()
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
        binding.rvChildtask.adapter = adapter
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this@EditTaskActivity)
        builder.setMessage(R.string.validation_delete)
        builder.setCancelable(true)
        builder.setPositiveButton(
            R.string.yes
        ) { _, _ ->
            task?.let { task ->
                viewModel.deleteTask(task.id)
            }
            Toast.makeText(this, R.string.text_delete, Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton(
            R.string.no
        ) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
