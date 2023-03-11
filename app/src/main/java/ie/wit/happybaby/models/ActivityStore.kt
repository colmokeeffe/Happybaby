package ie.wit.happybaby.models

interface ActivityStore {
    suspend fun findAll(): List<ActivityModel>
    suspend fun create(activity: ActivityModel)
    suspend fun update(activity: ActivityModel)
    suspend fun delete(activity: ActivityModel)
    suspend fun findActivityById(activityId: Long): ActivityModel?
    suspend fun findActivitiesByCategory(activityCategory: String): List<ActivityModel>
    suspend fun clear()
}