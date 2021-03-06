package com.csd051.superiora.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {
    fun getFilteredQuery(courseId: Int, filter: TasksFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM tasks ")
        when (filter) {
            TasksFilterType.COMPLETED_TASKS -> {
                simpleQuery.append("WHERE isDone = 1 AND id_course = $courseId ORDER BY id ASC")
            }
            TasksFilterType.ACTIVE_TASKS -> {
                simpleQuery.append("WHERE isDone = 0 AND id_parent = -1 AND id_course = $courseId ORDER BY id ASC")
            }
            TasksFilterType.FAVORITE_TASKS -> {
                simpleQuery.append("WHERE isFavorite = 1 AND id_parent = -1 AND id_course = $courseId ORDER BY id ASC")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}
