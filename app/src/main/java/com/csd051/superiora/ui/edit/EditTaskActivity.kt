package com.csd051.superiora.ui.edit

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.ActivityEditTaskBinding
import com.csd051.superiora.ui.detail.DetailTaskActivity
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.utils.DatePickerFragment
import com.csd051.superiora.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var viewModel : EditTaskViewModel
    private lateinit var binding: ActivityEditTaskBinding

    private var dueDateMillis: Long = System.currentTimeMillis()
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.edit_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EditTaskViewModel::class.java]

        binding.rvChildtask.layoutManager = LinearLayoutManager(this)

        getData()

        binding.btnSave.setOnClickListener {
            updateTask()
        }

        binding.addChild.setOnClickListener {
            addChild()
        }

        viewModel.getChildTask(task?.id ?: -2).observe(this, { tasks ->
            showRecyclerView(tasks)
        })

    }

    private fun getData() {
        task = intent.getParcelableExtra(EXTRA_DATA)
        task?.let { task ->
            binding.addEdTitle.setText(task.title)
            if(task.dueDate.equals("")) {
                binding.addTvDueDate.text = getString(R.string.due_date)
            } else {
                binding.addTvDueDate.text = task.dueDate
            }
            binding.addEdTriggerlink.setText(task.triggerLink)
            binding.addEdDescription.setText(task.details)
        }

        if (task?.id_course != 0) {
            binding.apply {
                addChild.visibility = View.GONE
                edtNewChildName.visibility = View.GONE
                addEdTitle.isEnabled = false
                addEdTriggerlink.isEnabled = false
                addEdDescription.isEnabled = false
            }

        }
    }

    private fun updateTask() {
        if(binding.addEdTitle.text.toString().isNotEmpty()) {
            task.let {
                task?.title = binding.addEdTitle.text.toString()
                if(binding.addTvDueDate.text.equals("Due Date")) {
                    task?.dueDate = ""
                } else {
                    task?.dueDate = binding.addTvDueDate.text.toString()
                }
                task?.triggerLink = binding.addEdTriggerlink.text.toString()
                task?.details = binding.addEdDescription.text.toString()
            }
            task?.let { task -> viewModel.updateTask(task) }
            Toast.makeText(this, R.string.task_updated, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailTaskActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(DetailTaskActivity.EXTRA_DATA, task)
            startActivity(intent)
        } else {
            binding.addEdTitle.error = getString(R.string.tv_field_notnull)
            binding.addEdTitle.requestFocus()
        }
    }

    private fun addChild() {
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
        val purge : (Task) -> Unit = { task ->
            purgeObject(task)
        }
        val doneTask: (Task, Boolean) -> Unit = { task, isDone ->
            doneTask(task, isDone)
        }
        val adapter = EditTaskAdapter(purge, doneTask)

        adapter.setListTask(tasks)
        binding.rvChildtask.adapter = adapter
    }

    private fun purgeObject(task: Task) {
        viewModel.getChildTask(task.id).observe(this, { child ->
            println(task.id)
            if(child.isNotEmpty()){
                for(each in child) {
                    purgeObject(each)
                }
            }
        })
        viewModel.deleteTask(task)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this@EditTaskActivity)
        builder.setMessage(R.string.validation_delete)
        builder.setCancelable(true)
        builder.setPositiveButton(
            R.string.yes
        ) { _, _ ->
            task?.let { task ->
                purgeObject(task)
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

    private fun doneTask(task: Task, isDone: Boolean) {
        AppExecutors().diskIO().execute {
            with(viewModel.getStaticChild(task.id)) {
                if (this.isNotEmpty()) {
                    for (each in this) {
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
                if (this.isNotEmpty()) {
                    for (each in this) {
                        doneByParent(each, isDone)
                    }
                }
            }
        }
        task.isDoneByParent = isDone
        viewModel.updateTask(task)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
