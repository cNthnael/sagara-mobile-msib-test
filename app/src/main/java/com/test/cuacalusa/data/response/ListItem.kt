package com.test.cuacalusa.data.response

import com.google.gson.annotations.SerializedName

data class ListItem(
    @field:SerializedName("dt")
    val dt: Int? = null,

    @field:SerializedName("pop")
    val pop: Double? = null,

    @field:SerializedName("visibility")
    val visibility: Int? = null,

    @field:SerializedName("dt_txt")
    val dtTxt: String? = null,

    @field:SerializedName("weather")
    val weather: List<WeatherItem?>? = null,

    @field:SerializedName("main")
    val main: Main,

    @field:SerializedName("clouds")
    val clouds: Clouds? = null,

    @field:SerializedName("sys")
    val sys: Sys? = null,

    @field:SerializedName("wind")
    val wind: Wind? = null
)
