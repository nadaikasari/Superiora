package com.csd051.superiora.ui.home.roadmaps

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.databinding.TaskItemBinding
import com.csd051.superiora.helper.TaskDiffCallback
import com.csd051.superiora.ui.detail.DetailTaskActivity
import com.csd051.superiora.ui.home.TaskTitleView
import com.csd051.superiora.ui.home.TaskTitleView.Companion.DONE
import com.csd051.superiora.ui.home.TaskTitleView.Companion.NORMAL
import com.csd051.superiora.ui.home.TaskTitleView.Companion.OVERDUE
import java.util.*

class RoadmapsAdapter(
    private val ctx: LifecycleOwner,
    private val roadmapViewModel : RoadmapsViewModel,
    private val doneTask: (Task, Boolean) -> Unit) : RecyclerView.Adapter<RoadmapsAdapter.TaskViewHolder>() {

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
        val task = listTask[position]
        holder.bind(task)
        when {
            task.isDone || task.isDoneByParent -> {
                holder.cbComplete.isChecked = true
                holder.tvTitle.state = DONE
            }
//            task.dueDate < System.currentTimeMillis() -> {
//                //OVERDUE
//                holder.cbComplete.isChecked = false
//                holder.tvTitle.state = OVERDUE
//
//            }
            else -> {
                //NORMAL
                holder.cbComplete.isChecked = false
                holder.tvTitle.state = NORMAL
            }
        }
    }

    override fun getItemCount(): Int {
        return listTask.size
    }


    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val tvTitle: TaskTitleView = binding.tvItemTitle
        val cbComplete: CheckBox = binding.cbItem

        fun bind(task: Task) {
            with(binding) {
                tvItemTitle.text = task.title
                tvItemDesc.text = task.dueDate
                itemContainer.setOnClickListener {
                    val intent = Intent(it.context, DetailTaskActivity::class.java)
                    intent.putExtra(DetailTaskActivity.EXTRA_DATA, task)
                    it.context.startActivity(intent)
                }
                val adapterTask = RoadmapsAdapter(ctx, roadmapViewModel) {task, isDone ->
                    doneTask(task, isDone)
                }

                roadmapViewModel.getChildTask(task.id).observe(ctx, { listTask ->
                    if (listTask.isNotEmpty()) {
                        adapterTask.setListTask(listTask)
                        dropdown.visibility = View.VISIBLE
                    }
                })

                with(rvChild) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = adapterTask
                }

                if (rvChild.visibility == View.VISIBLE) {
                    dropdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dropdown_up, 0,0,0)
                } else {
                    dropdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_drop_down, 0,0,0)
                }

                dropdown.setOnClickListener {
                    if (rvChild.visibility == View.VISIBLE) {
                        rvChild.visibility = View.GONE
                        dropdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_drop_down, 0,0,0)
                    } else {
                        rvChild.visibility = View.VISIBLE
                        dropdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dropdown_up, 0,0,0)
                    }
                }

                cbItem.setOnClickListener{
                    //Benerin dlu
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