package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

enum class AsteroidApiFilter() {
    SHOW_WEEK, SHOW_TODAY, SHOW_ALL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private var filter: MutableLiveData<AsteroidApiFilter> =
        MutableLiveData(AsteroidApiFilter.SHOW_ALL)


    fun setFilter(f: AsteroidApiFilter) {
        filter.value = f
    }

    val asteroids: LiveData<List<Asteroid>> = filter.switchMap {
        when (it) {
            AsteroidApiFilter.SHOW_TODAY -> Transformations.map(asteroidsRepository.getTodayAsteroids()) {
                it.asDomainModel()
            }
            AsteroidApiFilter.SHOW_WEEK -> Transformations.map(asteroidsRepository.getWeekAsteroids()) {
                it.asDomainModel()
            }
            else -> Transformations.map(asteroidsRepository.getAsteroids()) {
                it.asDomainModel()
            }
        }
    }


    init {
        viewModelScope.launch {
            asteroidsRepository.loadImageOfTheDay()
            asteroidsRepository.loadAsteroids()
        }
    }

    val pictureOfDay = asteroidsRepository.pictureOfDay


    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }


    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("unable to construct MainViewModel")
        }
    }

}


