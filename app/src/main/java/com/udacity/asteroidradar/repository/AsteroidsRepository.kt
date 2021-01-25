package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getCurrentDay
import com.udacity.asteroidradar.api.getNextWeekDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.lang.Exception


class AsteroidsRepository(database: Any) {

    val asteroids: MutableLiveData<List<Asteroid>> = MutableLiveData()

    fun loadAsteroids() {
        val currentDay = getCurrentDay()
        val nextWeekDay = getNextWeekDay()

        Network.nasaNeoWS.getAsteroids(currentDay, nextWeekDay).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                //TODO error msg
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    val result = response.body()
                    result?.let {
                        asteroids.value = parseAsteroidsJsonResult(JSONObject(it))
                    }
                } catch (e: Exception) {
                    //TODO error msg
                }
            }
        })
    }

    val pictureOfDay: MutableLiveData<PictureOfDay> = MutableLiveData()
    suspend fun loadImageOfTheDay() {
        val temp = Network.nasaNeoWS.getImageOfTheDay().await()
        pictureOfDay.value = temp
    }


}

