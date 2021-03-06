package com.csd051.superiora.ui.edit

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.TaskItemSimpleBinding
import com.csd051.superiora.helper.TaskDiffCallback
import java.util.*

class EditTaskAdapter(
    private val deleteTask: (Task) -> Unit,
    private val doneTask: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<EditTaskAdapter.TaskViewHolder>() {

    private val listTask = ArrayList<Task>()

    fun setListTask(listTask: List<Task>) {
        val diffCallback = TaskDiffCallback(this.listTask, listTask)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listTask.clear()
        this.listTask.addAll(listTask)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val listTask =
            TaskItemSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(listTask)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(listTask[position])
    }

    override fun getItemCount(): Int {
        return listTask.size
    }


    inner class TaskViewHolder(private val binding: TaskItemSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            with(binding) {
                tvItemTitle.text = task.title
                tvItemTitle.setCompoundDrawables(null, null, null,null)
                tvItemTitle.setOnClickListener {
                    val intent = Intent(it.context, EditTaskActivity::class.java)
                    intent.putExtra(EditTaskActivity.EXTRA_DATA, task)
                    it.context.startActivity(intent)
                }

                if(task.id_course != 0) {
                    deleteTask.visibility = View.GONE
                } else {
                    deleteTask.setOnClickListener {
                        deleteTask(task)
                    }
                }

                cbItem.isChecked = task.isDone

                cbItem.setOnClickListener{
                    if(task.isDone) {
                        doneTask(task, false)
                    } else {
                        doneTask(task, true)
                    }
                }
            }
        }

    }

}