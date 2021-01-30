package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DataBaseAsteroid)

    @Query("select * from databaseasteroid where closeApproachDate >= :currentDay order by closeApproachDate")
    fun getAsteroids(currentDay: String): LiveData<List<DataBaseAsteroid>>

    @Query("delete from databaseasteroid where closeApproachDate < :currentDay")
    fun deleteAsteroids(currentDay: String): Int


}

@Database(entities = [DataBaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    }

    return INSTANCE
}