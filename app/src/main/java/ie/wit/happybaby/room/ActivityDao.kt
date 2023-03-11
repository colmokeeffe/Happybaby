package ie.wit.happybaby.room

import androidx.room.*
import ie.wit.happybaby.models.ActivityModel

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(activity: ActivityModel)

    @Query("SELECT * FROM ActivityModel")
    suspend fun findAll(): List<ActivityModel>

    @Query("select * from ActivityModel where id = :activityId")
    suspend fun findById(activityId: Long): ActivityModel

    @Query("select * from ActivityModel where category = :activityCategory")
    suspend fun findByCategory(activityCategory: String): List<ActivityModel>

    @Update
    suspend fun update(activity: ActivityModel)

    @Delete
    suspend fun deleteActivity(activity: ActivityModel)
}