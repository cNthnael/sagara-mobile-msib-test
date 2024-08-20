package com.test.cuacalusa.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.test.cuacalusa.BuildConfig
import com.test.cuacalusa.R
import com.test.cuacalusa.data.response.WeatherResponse
import com.test.cuacalusa.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var forecastAdapter: ForecastListAdapter
    private lateinit var locationManager: LocationManager

    private val mainViewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission needed!", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Location permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showLoadingIndicator(true)

        forecastAdapter = ForecastListAdapter()
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = forecastAdapter
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        mainViewModel.weatherData.observe(this) { weather ->
            weather?.let {
                setWeatherData(it)
            }
        }

        mainViewModel.forecastData.observe(this) { forecast ->
            forecast?.let {
                forecastAdapter.submitList(it.list)
            }
        }

        binding.tvLocation.setOnClickListener {
            showLoadingIndicator(true)
            recreate()
        }

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @Suppress("DEPRECATION")
    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, { location ->
                location.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    updateBackgroundBasedOnTime()
                    mainViewModel.fetchWeatherData(latitude, longitude, BuildConfig.API_KEY)
                    mainViewModel.fetchForecastData(latitude, longitude, BuildConfig.API_KEY)
                }
            }, Looper.getMainLooper())
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale", "CheckResult")
    private fun setWeatherData(weather: WeatherResponse) {
        val tempInCelsius = (weather.main.temp - 273.15)
        binding.apply {
            tvLocation.text = weather.name
            tvTimestamp.text = "Last updated on\n ${convertTimestampToDate(System.currentTimeMillis() / 1000L)}"
            Glide.with(this@MainActivity)
                .load("https://openweathermap.org/img/wn/${weather.weather[0].icon}@4x.png")
                .into(ivCurrWeatherIcon)
            tvCurrTemp.text = String.format("%d°C", tempInCelsius.toInt())
            tvCurrWeatherType.text = weather.weather[0].main
            tvCurrHum.text = weather.main.humidity.toString() + "%"
            tvCurrWind.text = weather.wind.speed.toString() + " km/h"
        }
        showLoadingIndicator(false)
    }

    private fun convertTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000L)
        val format = SimpleDateFormat("EEEE, dd MMMM yyyy 'at' HH:mm", Locale.ENGLISH)
        return format.format(date)
    }

    private fun showLoadingIndicator(show: Boolean) {
        binding.apply {
            if (show) {
                tvLocation.visibility = android.view.View.INVISIBLE
                currWeatherIcon.visibility = android.view.View.GONE
                currWeatherDetails.visibility = android.view.View.GONE
                rvForecast.visibility = android.view.View.GONE
                loadingBar.visibility = android.view.View.VISIBLE
            } else {
                loadingBar.visibility = android.view.View.GONE
                tvLocation.visibility = android.view.View.VISIBLE
                currWeatherIcon.visibility = android.view.View.VISIBLE
                currWeatherDetails.visibility = android.view.View.VISIBLE
                rvForecast.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun updateBackgroundBasedOnTime() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        val layout = findViewById<ConstraintLayout>(R.id.weather_main)

        if (hourOfDay >= 18 || hourOfDay < 6) {
            layout.setBackgroundResource(R.drawable.main2_bg)
        } else {
            layout.setBackgroundResource(R.drawable.main_bg)
        }
    }

}
