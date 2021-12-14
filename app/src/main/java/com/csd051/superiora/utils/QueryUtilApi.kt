package com.csd051.superiora.utils

object QueryUtilApi {

    fun pathName(courseId: Int): String {
        return when (courseId) {
            2 -> "Angular"
            3 -> "Backend"
            4 -> "DBA"
            5 -> "DevOps"
            6 -> "FrontEnd"
            7 -> "Go"
            8 -> "Java"
            9 -> "Python"
            10 -> "React"
            else -> "Android"
        }
    }
}