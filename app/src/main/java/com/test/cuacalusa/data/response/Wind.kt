package com.test.cuacalusa.data.response

import com.google.gson.annotations.SerializedName

data class Wind(
    @field:SerializedName("deg")
    val deg: Int? = null,

    @field:SerializedName("speed")
    val speed: Any? = null,

    @field:SerializedName("gust")
    val gust: Any? = null
)
