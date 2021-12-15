package com.csd051.superiora.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.di.Injection
import com.csd051.superiora.ui.add.AddTaskViewModel
import com.csd051.superiora.ui.detail.DetailTaskViewModel
import com.csd051.superiora.ui.edit.EditTaskViewModel
import com.csd051.superiora.ui.home.roadmaps.RoadmapsViewModel
import com.csd051.superiora.ui.home.today_schedule.TodayScheduleViewModel
import com.csd051.superiora.ui.home.yourtask.YourTaskViewModel

class ViewModelFactory private constructor(private val repository: SuperioraRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddTaskViewModel::class.java) -> {
                AddTaskViewModel(repository) as T
            }
            modelClass.isAssignableFrom(YourTaskViewModel::class.java) -> {
                YourTaskViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TodayScheduleViewModel::class.java) -> {
                TodayScheduleViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditTaskViewModel::class.java) -> {
                EditTaskViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RoadmapsViewModel::class.java) -> {
                RoadmapsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailTaskViewModel::class.java) -> {
                DetailTaskViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

}