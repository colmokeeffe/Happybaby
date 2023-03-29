/*package ie.wit.happybaby.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ActivityMemStore: ActivityStore {

    val activities = ArrayList<ActivityModel>()

    override suspend fun findAll(): List<ActivityModel> {
        return activities
    }

    override suspend fun create(activity: ActivityModel) {
        activity.id = getId()
        activities.add(activity)
        logAll()
    }

    override suspend fun update(activity: ActivityModel) {
        var foundActivity: ActivityModel? = activities.find { t -> t.id == activity.id }
        if (foundActivity != null) {
            foundActivity.title = activity.title
            foundActivity.description = activity.description
            foundActivity.category = activity.category
            foundActivity.rating = activity.rating
            foundActivity.priority = activity.priority
            logAll()
        }
    }

    override suspend fun findActivityById(activityid: Long): ActivityModel? {
        var foundActivity: ActivityModel? = activities.find { t -> t.id == activityid }
        return foundActivity
    }

    override suspend fun findActivitiesByCategory(activityCategory: String): List<ActivityModel> {
        var filteredlist: MutableList<ActivityModel>
        var activities = findAll().toMutableList()

        when (activityCategory) {
            "all" -> {
                filteredlist = activities
            }
            "Poop" -> {
                filteredlist = activities.filter { it.category == "Poop" } as MutableList<ActivityModel>
            }

            "Pee" -> {
                filteredlist = activities.filter { it.category == "Pee" } as MutableList<ActivityModel>
            }

            "Feed" -> {
                filteredlist = activities.filter { it.category == "Feed" } as MutableList<ActivityModel>
            }
            "High Priority" -> {
                filteredlist = activities.filter { it.priority } as MutableList<ActivityModel>
            }
            else -> {
                filteredlist = activities
            }
        }
        return filteredlist
    }

    override suspend fun delete(activity: ActivityModel) {
        activities.remove(activity)
    }

    fun logAll() {
        activities.forEach{ i("${it}") }
    }

    override suspend fun clear(){
        activities.clear()
    }

}

 */