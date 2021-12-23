package com.csd051.superiora.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.TaskItemSimpleBinding
import com.csd051.superiora.helper.TaskDiffCallback
import java.util.*

class DetailTaskAdapter(
    private val executeIntent: (Task) -> Unit
): RecyclerView.Adapter<DetailTaskAdapter.TaskViewHolder>() {

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

    inner class TaskViewHolder (private val binding: TaskItemSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            with(binding) {
                tvItemTitle.text = task.title
                cbItem.visibility = View.GONE
                deleteTask.visibility = View.GONE
                tvItemTitle.setOnClickListener {
                    executeIntent(task)
                }
            }
        }
    }
}