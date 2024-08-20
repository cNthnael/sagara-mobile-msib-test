package com.test.cuacalusa.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.cuacalusa.data.response.ListItem
import com.test.cuacalusa.databinding.ItemForecastBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastListAdapter :
    ListAdapter<ListItem, ForecastListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DefaultLocale")
        fun bind(item: ListItem) {
            val tempInCelsius = convertKelvinToCelsius(item.main.temp)
            val tempMaxInCelsius = convertKelvinToCelsius(item.main.tempMax)
            val tempMinInCelsius = convertKelvinToCelsius(item.main.tempMin)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("h a", Locale.ENGLISH)

            val date = item.dtTxt?.let { dateFormat.parse(it) }
            val dayOfWeek = date?.let { dayFormat.format(it) }
            val timeOfDay = date?.let { timeFormat.format(it) }

            binding.apply {
                tvForecastDay.text = dayOfWeek
                tvForecastTime.text = timeOfDay
                tvForecastTemp.text = String.format("%d°C", tempInCelsius.toInt())
                tvMaxFr.text = String.format("%d°C", tempMaxInCelsius.toInt())
                tvMinFr.text = String.format("%d°C", tempMinInCelsius.toInt())

                val iconCode = item.weather?.firstOrNull()?.icon
                val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@4x.png"

                Glide.with(itemView)
                    .load(iconUrl)
                    .override(220, 220)
                    .into(ivForecastIcon)
            }
        }



        private fun convertKelvinToCelsius(tempKelvin: Double?): Double {
            return tempKelvin?.minus(273.15) ?: 0.0
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem.dt == newItem.dt
            }
        }
    }

}