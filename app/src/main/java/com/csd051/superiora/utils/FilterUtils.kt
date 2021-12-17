package com.csd051.superiora.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {
    fun getFilteredQuery(filter: TasksFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM taskuser ")
        when (filter) {
            TasksFilterType.COMPLETED_TASKS -> {
                simpleQuery.append("WHERE isDone = 1 OR isDoneByParent = 1")
            }
            TasksFilterType.ACTIVE_TASKS -> {
                simpleQuery.append("WHERE isDone = 0 isDoneByParent = 0")
            }
            TasksFilterType.FAVORITE_TASKS -> {

            }
            else -> {
                // ALL_TASKS
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun nearestQuery(type: QueryType): SimpleSQLiteQuery {
        var query = ""
        when (type) {
            QueryType.CURRENT_DAY -> query = """
                 SELECT * FROM taskuser 
                 WHERE day = (strftime('%w', 'now') + 1)
                 AND strftime('%H:%M', startTime) > strftime('%H:%M', 'now')
                 ORDER BY strftime('%H:%M', startTime) ASC LIMIT 1
                 """

            QueryType.NEXT_DAY -> query = """
                 SELECT * FROM taskuser 
                 WHERE day > (strftime('%w', 'now') + 1)
                 ORDER BY day,strftime('%H:%M', startTime) ASC LIMIT 1
                 """

            QueryType.PAST_DAY -> query = """
                 SELECT * FROM taskuser 
                 WHERE day >= 0
                 ORDER BY day, strftime('%H:%M', startTime) ASC LIMIT 1
                 """
        }

        return SimpleSQLiteQuery(query)
    }
}

enum class QueryType {
    CURRENT_DAY,
    NEXT_DAY,
    PAST_DAY
}