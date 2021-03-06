package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getCurrentDay
import com.udacity.asteroidradar.api.getNextWeekDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class AsteroidsRepository(private val database: AsteroidsDatabase) {
    private val currentDay = getCurrentDay()
    private val nextWeekDay = getNextWeekDay()

    fun getAsteroids() = database.asteroidDao.getAsteroids(currentDay)

    fun getTodayAsteroids() = database.asteroidDao.getTodayAsteroids(currentDay)

    fun getWeekAsteroids() = database.asteroidDao.getWeekAsteroids(currentDay, nextWeekDay)


    fun loadAsteroids() {
        Network.nasaNeoWS.getAsteroids(currentDay, nextWeekDay).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("test123", "onFailure ${t}")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    val result = response.body()
                    result?.let {
                        val asteroidsList = parseAsteroidsJsonResult(JSONObject(it))

                        GlobalScope.launch {
                            database.asteroidDao.insertAll(*asteroidsList.asDatabaseModel())
                        }
                        //
                    }
                } catch (e: Exception) {
                    //TODO error msg
                }
            }
        })
    }

    fun deleteAsteroids() {
        GlobalScope.launch {
            database.asteroidDao.deleteAsteroids(currentDay)
        }
    }

    val pictureOfDay: MutableLiveData<PictureOfDay> = MutableLiveData()
    suspend fun loadImageOfTheDay() {
        try {
            val temp = Network.nasaNeoWS.getImageOfTheDay().await()
            pictureOfDay.value = temp
        } catch (e: Exception) {
            Log.i("test123", "loadImageOfTheDay onFailure ${e}")
        }
    }


}

