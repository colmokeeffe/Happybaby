package ie.wit.happybaby.models

interface ActivityStore {
    suspend fun findAll(): List<ActivityModel>
    suspend fun findAllGalleries(): List<ActivityGalleryModel>
    suspend fun create(activity: ActivityModel)
    suspend fun createGallery(gallery: ActivityGalleryModel)
    suspend fun update(activity: ActivityModel)
    suspend fun updateGallery(gallery: ActivityGalleryModel)
    suspend fun delete(activity: ActivityModel)
    suspend fun deleteGallery(gallery: ActivityGalleryModel)
    suspend fun findActivityById(activityId: Long): ActivityModel?
    suspend fun findActivityGalleryById(activityId: Long): ActivityGalleryModel?
    suspend fun findActivitiesByCategory(activityCategory: String): List<ActivityModel>
    suspend fun findActivitiesGalleryByCategory(activityGalleryCategory: String): List<ActivityGalleryModel>
    suspend fun clear()
    suspend fun clearGallery()
}