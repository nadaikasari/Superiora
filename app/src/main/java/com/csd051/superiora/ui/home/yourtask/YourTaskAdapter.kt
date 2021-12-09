package com.csd051.superiora.ui.home.yourtask

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.TaskItemBinding
import com.csd051.superiora.helper.TaskDiffCallback
import com.csd051.superiora.ui.edit.EditTaskActivity
import com.csd051.superiora.utils.ItemCallback
import java.util.ArrayList

class YourTaskAdapter(private val ctx: LifecycleOwner, private val yourTaskViewModel : YourTaskViewModel) : RecyclerView.Adapter<YourTaskAdapter.TaskViewHolder>() {

    private val listTask = ArrayList<Task>()

    fun setListTask(listTask: List<Task>) {
        val diffCallback = TaskDiffCallback(this.listTask, listTask)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listTask.clear()
        this.listTask.addAll(listTask)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val listTask = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(listTask)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(listTask[position])
    }

    override fun getItemCount(): Int {
        return listTask.size
    }


    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            with(binding) {
                tvItemTitle.text = task.title
                tvItemDesc.text = task.dueDate
                itemContainer.setOnClickListener {
                    val intent = Intent(it.context, EditTaskActivity::class.java)
                    intent.putExtra(EditTaskActivity.EXTRA_DATA, task)
                    it.context.startActivity(intent)
                }
                val adapterTask = YourTaskAdapter(ctx, yourTaskViewModel)

                yourTaskViewModel.getChildTask(task.id).observe(ctx, { listTask ->
                    if (listTask != null) {
                        adapterTask.setListTask(listTask)
                    }
                })

                with(rvChild) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = adapterTask
                }

                btnDropdown.setOnClickListener {
                    if (rvChild.visibility == View.VISIBLE) {
                        rvChild.visibility = View.GONE
                    } else {
                        rvChild.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }




}