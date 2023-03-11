package ie.wit.happybaby.models

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.wit.happybaby.helpers.readImageFromPath
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ActivityFireStore(val context: android.content.Context) : ActivityStore {
    val activities = ArrayList<ActivityModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override suspend fun findAll(): List<ActivityModel> {
        return activities
    }

    fun generateRandomId(): Long {
        return Random().nextLong()
    }

    override suspend fun findActivityById(id: Long): ActivityModel? {
        val foundActivity: ActivityModel? = activities.find { p -> p.id == id }
        return foundActivity
    }

    override suspend fun findActivitiesByCategory(activityCategory: String): List<ActivityModel> {
        var filteredlist: MutableList<ActivityModel>
        var activities = findAll().toMutableList()

        when (activityCategory) {
            "All" -> {
                filteredlist = activities
            }
            "Poo" -> {
                filteredlist = activities.filter { it.category == "Poo" } as MutableList<ActivityModel>
            }

            "Pee" -> {
                filteredlist = activities.filter { it.category == "Pee" } as MutableList<ActivityModel>
            }

            "Exercise" -> {
                filteredlist = activities.filter { it.category == "Exercise" } as MutableList<ActivityModel>
            }

            "Medication" -> {
                filteredlist = activities.filter { it.category == "Medication" } as MutableList<ActivityModel>
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

    override suspend fun create(activity: ActivityModel) {
        val key = db.child("users").child(userId).child("activities").push().key
        key?.let {
            activity.fbId = key
            activity.id = generateRandomId() //note that this is also needed as used for marker on map of all placemarks
            activities.add(activity)
            db.child("users").child(userId).child("activities").child(key).setValue(activity)
        }
    }

    override suspend fun update(activity: ActivityModel) {
        var foundActivity: ActivityModel? = activities.find { p -> p.fbId == activity.fbId }
        if (foundActivity != null) {
            foundActivity.description = activity.description
            foundActivity.category = activity.category
            foundActivity.rating = activity.rating
            foundActivity.priority = activity.priority
            foundActivity.hour = activity.hour
            foundActivity.minute = activity.minute
            foundActivity.date = activity.date
            foundActivity.selectedTime = activity.selectedTime
            foundActivity.selectedDate = activity.selectedDate
        }
        db.child("users").child(userId).child("activities").child(activity.fbId).setValue(activity)
    }

    override suspend fun delete(activity: ActivityModel) {
        db.child("users").child(userId).child("activities").child(activity.fbId).removeValue()
        activities.remove(activity)
    }

    override suspend fun clear() {
        activities.clear()
    }

    fun fetchActivities(activitiesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(activities) {
                    it.getValue<ActivityModel>(
                        ActivityModel::class.java
                    )
                }
                activitiesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        activities.clear()
        db.child("users").child(userId).child("activities")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}