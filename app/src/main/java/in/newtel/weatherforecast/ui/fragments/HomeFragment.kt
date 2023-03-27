package `in`.newtel.weatherforecast.ui.fragments

import `in`.newtel.weatherforecast.R
import `in`.newtel.weatherforecast.databinding.FragmentHomeBinding
import `in`.newtel.weatherforecast.util.convertToListOfCityName
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import `in`.newtel.weatherforecast.model.WeatherInfoShowModelImpl
import `in`.newtel.weatherforecast.model.data_class.City
import `in`.newtel.weatherforecast.model.model.WeatherInfoShowModel
import `in`.newtel.weatherforecast.model.model.data_class.WeatherData


class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var model: WeatherInfoShowModel
    private lateinit var viewModel: HomeViewModel

    private var cityList: MutableList<City> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        model = WeatherInfoShowModelImpl(requireActivity())
        // initialize ViewModel
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // set LiveData and View click listeners before the call for data fetching
        setLiveDataListeners()
        setViewClickListener()


        viewModel.getCityList(model)

        return binding.root
    }

    private fun setViewClickListener() {
        // View Weather button click listener
        binding.layoutInput.btnViewWeather.setOnClickListener {
            val selectedCityId = cityList[binding.layoutInput.spinner.selectedItemPosition].id
            viewModel.getWeatherInfo(selectedCityId, model) // fetch weather info
        }
    }

    private fun setLiveDataListeners() {


        viewModel.cityListLiveData.observe(requireActivity(),
            Observer { cities -> setCityListSpinner(cities) })


        viewModel.cityListFailureLiveData.observe(requireActivity(), Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        })


        viewModel.progressBarLiveData.observe(requireActivity(), Observer { isShowLoader ->
            if (isShowLoader)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        })


        viewModel.weatherInfoLiveData.observe(requireActivity(), Observer { weatherData ->
            setWeatherInfo(weatherData)
        })


        viewModel.weatherInfoFailureLiveData.observe(requireActivity(), Observer { errorMessage ->
            binding.outputGroup.visibility = View.GONE
            binding.tvErrorMessage.visibility = View.VISIBLE
            binding.tvErrorMessage.text = errorMessage
        })
    }

    private fun setCityListSpinner(cityList: MutableList<City>) {
        this.cityList = cityList

        val arrayAdapter = ArrayAdapter(
            requireContext(), R.layout.support_simple_spinner_dropdown_item,
            this.cityList.convertToListOfCityName()
        )

        binding.layoutInput.spinner.adapter = arrayAdapter
        binding.layoutInput.spinner.onItemSelectedListener = this@HomeFragment

    }

    private fun setWeatherInfo(weatherData: WeatherData) {
        binding.outputGroup.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.GONE

        binding.layoutWeatherBasic.tvDateTime?.text = weatherData.dateTime
        binding.layoutWeatherBasic.tvTemperature?.text = weatherData.temperature
        binding.layoutWeatherBasic.tvCityCountry?.text = weatherData.cityAndCountry
        Glide.with(this).load(weatherData.weatherConditionIconUrl)
            .into(binding.layoutWeatherBasic.ivWeatherCondition)
        binding.layoutWeatherBasic.tvWeatherCondition?.text =
            weatherData.weatherConditionIconDescription

        binding.layoutWeatherAdditional.tvHumidityValue?.text = weatherData.humidity
        binding.layoutWeatherAdditional.tvPressureValue?.text = weatherData.pressure
        binding.layoutWeatherAdditional.tvVisibilityValue?.text = weatherData.visibility

        binding.layoutSunsetSunrise.tvSunriseTime?.text = weatherData.sunrise
        binding.layoutSunsetSunrise.tvSunsetTime?.text = weatherData.sunset
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedCityId = cityList[binding.layoutInput.spinner.selectedItemPosition].id
        viewModel.getWeatherInfo(selectedCityId, model) // fetch weather info
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}