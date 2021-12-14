package com.csd051.superiora.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class TaskResponse(
	@field:SerializedName("TaskResponse")
	val taskResponse: List<Task>? = null
) : Parcelable
