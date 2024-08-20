package com.test.cuacalusa.data.response

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
	@field:SerializedName("city")
	val city: City,

	@field:SerializedName("cnt")
	val cnt: Int? = null,

	@field:SerializedName("cod")
	val cod: String,

	@field:SerializedName("message")
	val message: Int,

	@field:SerializedName("list")
	val list: List<ListItem>
)
