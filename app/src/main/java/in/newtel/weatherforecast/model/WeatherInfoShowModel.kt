package `in`.newtel.weatherforecast.model.model

import `in`.newtel.weatherforecast.util.RequestCompleteListener
import `in`.newtel.weatherforecast.model.data_class.City
import `in`.newtel.weatherforecast.model.model.data_class.WeatherInfoResponse

interface WeatherInfoShowModel {
    fun getCityList(callback: RequestCompleteListener<MutableList<City>>)
    fun getWeatherInfo(cityId: Int, callback: RequestCompleteListener<WeatherInfoResponse>)
}