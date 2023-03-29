/*package ie.wit.happybaby.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.happybaby.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "activities.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<ActivityModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ActivityJSONStore(private val context: Context) : ActivityStore {

    var activities = mutableListOf<ActivityModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun findAll(): MutableList<ActivityModel> {
        logAll()
        return activities
    }

    override suspend fun create(activity: ActivityModel) {
        activity.id = generateRandomId()
        activities.add(activity)
        serialize()
    }


    override suspend fun update(activity: ActivityModel) {
        var foundActivity: ActivityModel? = activities.find { t -> t.id == activity.id }
        if (foundActivity != null) {
            foundActivity.title = activity.title
            foundActivity.description = activity.description
            foundActivity.category = activity.category
            foundActivity.rating = activity.rating
            foundActivity.priority = activity.priority
            serialize()
        }
    }

    override suspend fun findActivityById(activityId: Long): ActivityModel? {
        var foundActivity: ActivityModel? = activities.find { t -> t.id == activityId }
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

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(activities, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        activities = gsonBuilder.fromJson(jsonString, listType)
    }

    override suspend fun delete(activity: ActivityModel) {
        activities.remove(activity)
        serialize()
    }

    private fun logAll() {
        activities.forEach { Timber.i("$it") }
    }

    override suspend fun clear(){
        activities.clear()
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}

 */