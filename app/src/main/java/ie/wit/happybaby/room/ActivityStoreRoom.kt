package ie.wit.happybaby.room

import android.content.Context
import androidx.room.Room
import ie.wit.happybaby.models.ActivityModel
import ie.wit.happybaby.models.ActivityStore

class ActivityStoreRoom(val context: Context) : ActivityStore {

    var dao: ActivityDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.activityDao()
    }

    override suspend fun findAll(): List<ActivityModel> {
        return dao.findAll()
    }

    override suspend fun findActivityById(activityId: Long): ActivityModel? {
        return dao.findById(activityId)
    }

    override suspend fun findActivitiesByCategory(activityCategory: String): List<ActivityModel> {
        if (activityCategory == "All") {
            return dao.findAll()
        }
        return dao.findByCategory(activityCategory)
    }

    override suspend fun create(activity: ActivityModel) {
        dao.create(activity)
    }

    override suspend fun update(activity: ActivityModel) {
        dao.update(activity)
    }

    override suspend fun delete(activity: ActivityModel) {
        dao.deleteActivity(activity)
    }

    override suspend fun clear() {
    }
}