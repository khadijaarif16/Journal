package com.memoryjournal.app
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.util.Log


//response from weatherapi
@Serializable
data class WeatherResponse(
    val weather: List<WeatherDesc>,
    val main: MainWeather
)
@Serializable
data class WeatherDesc(
    val description: String
)
@Serializable
data class MainWeather(
    val temp: Double
)
class WeatherService {
    //from http client
    private val client = HttpClient(Android){
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true //dont crash
            })
        }

    }

    //fetch wether, null is fails
    suspend fun getWeather(lat: Double, lon: Double): String?{
        return try{
            val apikey = "c999ea85c9e37fb7520e04376c18bc19"
            val res: WeatherResponse = client.get(
                "https://api.openweathermap.org/data/2.5/weather"
            ){
                parameter("lat",lat)
                parameter("lon",lon)
                parameter("appid",apikey)
                parameter("units","imperial") //F
            }.body()
            //fromat response
            val description = res.weather.firstOrNull()?.description
                ?.replaceFirstChar {it.uppercase()}?: "Unknown"

            val temp = res.main.temp.toInt()

            val result = "$description, ${temp}°F"
            Log.d("WeatherService", "Weather fetched: $result")
            return result

        }catch (e: Exception) {
            Log.e("WeatherService", "Weather error: ${e.message}")
            null // return null if weather fetch fails — entry still saves
        }

    }
}
