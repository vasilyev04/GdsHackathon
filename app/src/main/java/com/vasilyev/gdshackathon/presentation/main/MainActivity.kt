package com.vasilyev.gdshackathon.presentation.main

import android.Manifest
import android.R
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vasilyev.gdshackathon.data.repository.AiRepositoryImpl
import com.vasilyev.gdshackathon.databinding.ActivityMainBinding
import com.vasilyev.gdshackathon.domain.entity.Message
import com.vasilyev.gdshackathon.presentation.BottomSheetInfoFragment
import com.vasilyev.gdshackathon.presentation.main.adapter.PlacesRecyclerViewAdapter
import com.vasilyev.gdshackathon.presentation.main.adapter.RecyclerViewAdapter
import com.vasilyev.gdshackathon.presentation.map.MapActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var userLocation: Location

    private val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val adapter = RecyclerViewAdapter()
    private val placeAdapter = PlacesRecyclerViewAdapter()


    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory(application)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        var isGranted = true

        permissions.entries.forEach {
            if(it.key in REQUIRED_PERMISSIONS){
                isGranted = it.value
            }
        }

        if(isGranted){
            showChat()
            observeState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()
        binding.rvPlaces.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaces.adapter = placeAdapter
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        placeAdapter.onItemClick = {
            val bottomSheetInfoFragment = BottomSheetInfoFragment().apply {
                val bundle = Bundle().apply {
                    putInt("EXTRA_PLACE", it)
                }
                arguments = bundle
            }
            bottomSheetInfoFragment.show(supportFragmentManager, "Bottom Sheet Info Fragment")
        }
    }


    private fun requestPermissions(){
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun observeState(){
        viewModel.mainState.observe(this){state ->
            when(state){
                is MainState.Places -> {
                    placeAdapter.submitList(state.list)
                }

                is MainState.AiAnswer -> {
                    adapter.addMessage(Message("ai", state.answer))
                }

                is MainState.PlaceReceived -> {

                }
            }
        }
    }

    private fun showChat(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){}

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0F, gpsLocationListener)


        val startPrompt = "1.Океанариум «Ailand»:\n" +
                "Адрес: Астана, Коргалжинское шоссе, 2\n" +
                "Автобусы: № 12 , 18 , 21 , 27 , 28 , 35 , 42 , 43 , 44\n" +
                "График работы: Ежедневно с 10:00 до 20:00\n" +
                "Контакты: +8 (7172) 57 55 55\n" +
                "2.Монумент «Астана-Байтерек»: \n" +
                "Адрес: Астана, Нуржол бульвар, 14\n" +
                "Автобусы: № 14 , 21 , 28 , 46\n" +
                "График работы: Ежедневно с 09:00 до 21:00\n" +
                "Контакты: +7 (7172) 44 64 72\n" +
                "3.Театр аниматрониксов «Джунгли»:\n" +
                "Адрес: г. Астана, Коргалжинское шоссе, 2\n" +
                "Автобусы: № 12 , 18 , 21 , 27 , 28 , 35 , 42 , 43 , 44\n" +
                "График работы: Пн-Пт с 10:00 до 20:00\n" +
                "Контакты: +7 (7172) 24-01-05\n" +
                "4.Колесо обозрения «Ailand»:\n" +
                "Адрес: г. Астана, Коргалжинское шоссе, 2\n" +
                "Автобусы: № 12 , 18 , 21 , 27 , 28 , 35 , 42 , 43 , 44\n" +
                "График работы: Ежедневно с 10:00 до 20:00\n" +
                "Контакты: +8 (7172) 24 22 22\n" +
                "5.Музей энергии будущего «Нур-Алем» (ЭКСПО):\n" +
                "Адрес: Астана, Орынбор , 55.\n" +
                "Автобусы: № 504 , 506 , 500 , 502 , 501 , 505\n" +
                "График работы: Вт - Вс с 10:00 до 20:00\n" +
                "Контакты: +8 (7172) 91 71 23\n" +
                "6.Национальный музей Республики Казахстан:\n" +
                "Адрес: Астана, пр. Тәуелсіздік, 54\n" +
                "Автобусы: № 10 , 12 , 18 , 21 , 27 , 28 , 35 , 41 , 47\n" +
                "График работы: Вт-Сб с 10:00 - до 18:00\n" +
                "Контакты: +8 (7172) 91 90 35\n" +
                "7.Дворец Мира и Согласия:\n" +
                "Адрес: Астана, проспект Тауелсiздiк 57\n" +
                "Автобусы: № 4 , 14 , 19 , 21 , 29 , 40\n" +
                "График работы: Вт-Сб с 10:00 - до 18:00\n" +
                "Контакты: +8 (7172) 74 47 44\n" +
                "8.Астана Опера:\n" +
                "Адрес: Астана, Кунаева 1\n" +
                "Автобусы: № 12 , 26 , 32 , 35 , 46 , 50 , 53\n" +
                "График работы: Ежедневно. Кроме понедельника в 11:00 и в 15:00\n" +
                "Контакты: +8 (7172) 70 96 00\n" +
                "9.Музейно-мемориальный комплекс «АЛЖИР»:\n" +
                "Адрес: г. Астана, с. Коргалжын, РГУ «Коргалжынский государственный природный заповедник», ул. М. Рахимжана, 20.\n" +
                "Автобусы: № 12 , 26 , 32\n" +
                "График работы: Вт - Вс с 09:00 до 18:00\n" +
                "Контакты: +8 (7172) 49 94 55\n" +
                "10.Музей Сакена Сейфуллина:\n" +
                "Адрес: Астана, ул. Ауэзова, 20\n" +
                "Автобусы: № 2 , 3 , 5 , 8 , 10 , 17 , 19 , 20 , 23 , 26 , 27 , 33 , 34 , 37 , 40 , 43 , 53 , 64 , 71 , 73\n" +
                "График работы: Пн-Пт с 09:00 до 17:00\n" +
                "Контакты: +8 (7172) 32 84 67\n" +
                "11.Водные прогулки на теплоходах по реке Ишим:\n" +
                "Адрес: Астана, касса находится на набережной перед Дворцом Школьников и между ЖК Гранд Алатау\n" +
                "Автобусы: № 1 , 9 , 17 , 23 , 25 , 31 , 32 , 105\n" +
                "График работы: Пн-Вс с 12:00 до 20:30\n" +
                "Контакты: @esil.astanaa\n" +
                "12.Этно – мемориальный комплекс «Атамекен»:\n" +
                "Адрес: Астана, Коргалжинское шоссе, 2/1\n" +
                "Автобусы: № 32 , 44 , 28\n" +
                "График работы: Ежедневно с 10:00 до 19:00\n" +
                "Контакты: www.atamekenmap.kz\n" +
                "13.Триумфальная арка «Мәңгілік Ел»:\n" +
                "Адрес: Астана, пр. Мәңгілік Ел\n" +
                "Автобусы: № 15 , 18 , 35 , 70 , 314\n" +
                "График работы: Пн-Пт с 10:00 до 18:00\n" +
                "Контакты: +8 (776) 668 3321\n" +
                "\n" +
                "У меня в приложении имеется база данных с достопримечательностями, этот промпт нужен для того, чтобы пользователь моего приложения получал оптимальный список, состоящий как минимум из одной достопримечательности подходящей под его запрос. Ты дожен сам определить, какие из вышеперечисленных достопримечательностей больше подходят пользователю и ТОЛЬКО общую информацию: 1)Название достопримечательности 2) ее описание. Но если пользователь попросить уточнение, то только в таком случае используй дополнительные данные сверху.\n" +
                "\n" + "Пользователь находится в роли туриста, который хочет провести время в Астане и ищет для этого подходящее место, ты должен помочь\n"
                "В конце ответа пришли список из используемых достопримечательностей в виде {id, id...}, где id является порядковым номером достопремичательности из вышеперечисленного списка. Далее будут ответы от пользователя, не нужно отвечать на данный промпт." +
                "\n" +
                "Вот пример:\n" +
                "Пользователь - Я приехал в Астану и хочу посетить музей, что посоветуешь?\n" +
                "Ответ - \n" +
                "Музей энергии будущего «Нур-Алем» (ЭКСПО)\n" +
                "*краткое описание*\n" +
                "\n" +
                "Национальный музей Республики Казахстан\n" +
                "*краткое описание*\n" +
                "\n" +
                "Музейно-мемориальный комплекс «АЛЖИР»\n" +
                "*краткое описание*\n" +
                "\n" +
                "Музей Сакена Сейфуллина\n" +
                "*краткое описание*\n" +
                "\n" +
                "{5,6,9,10}" +
                "\n\n У тебя есть следующие ограничения: ты не должен допускать оффтопа в диалоге с пользователем, то есть если он задает вопрос не связанный с местами в городе Астана, то ты должен ответить пользователю что ты обучен отвечать только не тему достопримечательностей города Астаны"

    }


    private fun hideWelcomeText(){
        binding.llWelcome.visibility = View.GONE
        binding.rvChat.visibility = View.VISIBLE
        binding.rvChat.adapter = adapter
    }



    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }
}