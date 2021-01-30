package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.main.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getCurrentDay
import com.udacity.asteroidradar.api.getNextWeekDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
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
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(currentDay)) {
            it.asDomainModel()
        }

    fun loadAsteroids() {
        val nextWeekDay = getNextWeekDay()

        Network.nasaNeoWS.getAsteroids(currentDay, nextWeekDay).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                //TODO error msg
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
        val temp = Network.nasaNeoWS.getImageOfTheDay().await()
        pictureOfDay.value = temp
    }


}

