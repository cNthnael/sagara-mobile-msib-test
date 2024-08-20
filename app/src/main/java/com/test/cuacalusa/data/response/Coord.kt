package com.test.cuacalusa.data.response

import com.google.gson.annotations.SerializedName

data class Coord(
    @field:SerializedName("lon")
    val lon: Any? = null,

    @field:SerializedName("lat")
    val lat: Any? = null
)
