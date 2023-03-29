package ie.wit.happybaby.room

import androidx.room.*
import ie.wit.happybaby.models.ActivityGalleryModel
import ie.wit.happybaby.models.ActivityModel

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(activity: ActivityModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createGallery(gallery: ActivityGalleryModel)

    @Query("SELECT * FROM ActivityModel")
    suspend fun findAll(): List<ActivityModel>

    @Query("SELECT * FROM ActivityGalleryModel")
    suspend fun findAllGalleries(): List<ActivityGalleryModel>

    @Query("select * from ActivityModel where id = :activityId")
    suspend fun findById(activityId: Long): ActivityModel

    @Query("select * from ActivityGalleryModel where id = :activityId")
    suspend fun findByGalleryId(activityId: Long): ActivityGalleryModel

    @Query("select * from ActivityModel where category = :activityCategory")
    suspend fun findByCategory(activityCategory: String): List<ActivityModel>

    @Query("select * from ActivityGalleryModel where imagecategory = :activityCategory")
    suspend fun findByGalleryCategory(activityCategory: String): List<ActivityGalleryModel>

    @Update
    suspend fun update(activity: ActivityModel)

    @Update
    suspend fun updateGallery(gallery: ActivityGalleryModel)

    @Delete
    suspend fun deleteActivity(activity: ActivityModel)

    @Delete
    suspend fun deleteGallery(gallery: ActivityGalleryModel)
}