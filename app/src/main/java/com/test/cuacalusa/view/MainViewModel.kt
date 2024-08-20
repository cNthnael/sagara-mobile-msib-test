package com.test.cuacalusa.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.cuacalusa.data.api.ApiConfig
import com.test.cuacalusa.data.response.ForecastResponse
import com.test.cuacalusa.data.response.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData

    private val _forecastData = MutableLiveData<ForecastResponse?>()
    val forecastData: LiveData<ForecastResponse?> = _forecastData

    fun fetchWeatherData(latitude: Double, longitude: Double, apiKey: String) {
        val apiService = ApiConfig.getApiService()
        apiService.getWeather(latitude, longitude, apiKey)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        _weatherData.value = response.body()
                    } else {
                        _weatherData.value = null
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    _weatherData.value = null
                }
            })
    }

    fun fetchForecastData(latitude: Double, longitude: Double, apiKey: String) {
        val apiService = ApiConfig.getApiService()
        apiService.getForecast(latitude, longitude, 24, apiKey)
            .enqueue(object : Callback<ForecastResponse> {
                override fun onResponse(
                    call: Call<ForecastResponse>,
                    response: Response<ForecastResponse>
                ) {
                    if (response.isSuccessful) {
                        _forecastData.value = response.body()
                    } else {
                        _forecastData.value = null
                    }
                }

                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    _forecastData.value = null
                }
            })
    }
}
