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
    val galleries = ArrayList<ActivityGalleryModel>()
    lateinit var userId: String
    var db: DatabaseReference= FirebaseDatabase.getInstance().reference
    var st: StorageReference = FirebaseStorage.getInstance().reference


    override suspend fun findAll(): List<ActivityModel> {
        return activities
    }

    override suspend fun findAllGalleries(): List<ActivityGalleryModel> {
        return galleries
    }

    fun generateRandomId(): Long {
        return Random().nextLong()
    }

    override suspend fun findActivityById(id: Long): ActivityModel? {
        val foundActivity: ActivityModel? = activities.find { p -> p.id == id }
        return foundActivity
    }

    override suspend fun findActivityGalleryById(id: Long): ActivityGalleryModel? {
        val foundGalleryActivity: ActivityGalleryModel? = galleries.find { p -> p.galleryid == id }
        return foundGalleryActivity
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

    override suspend fun findActivitiesGalleryByCategory(activityGalleryCategory: String): List<ActivityGalleryModel> {
        var filteredGallerylist: MutableList<ActivityGalleryModel>
        var galleries = findAllGalleries().toMutableList()

        when (activityGalleryCategory) {
            "All" -> {
                filteredGallerylist = galleries
            }
            "Newborn" -> {
                filteredGallerylist = galleries.filter { it.imagecategory == "Newborn" } as MutableList<ActivityGalleryModel>
            }

            "3-6m" -> {
                filteredGallerylist = galleries.filter { it.imagecategory == "3-6m" } as MutableList<ActivityGalleryModel>
            }

            "6-9m" -> {
                filteredGallerylist = galleries.filter { it.imagecategory == "6-9m" } as MutableList<ActivityGalleryModel>
            }

            "9-12m" -> {
                filteredGallerylist = galleries.filter { it.imagecategory == "9-12m" } as MutableList<ActivityGalleryModel>
            }

            else -> {
                filteredGallerylist = galleries
            }
        }
        return filteredGallerylist
    }

    override suspend fun create(activity: ActivityModel) {
        val key = db.child("users").child(userId).child("activities").push().key
        key?.let {
            activity.fbId = key
            activity.id = generateRandomId()
            activities.add(activity)
            db.child("users").child(userId).child("activities").child(key).setValue(activity)
        }
    }

    override suspend fun createGallery(gallery: ActivityGalleryModel) {
        val key = db.child("users").child(userId).child("galleries").push().key
        key?.let {
            gallery.fbGalleryId = key
            gallery.galleryid = generateRandomId()
            galleries.add(gallery)
            db.child("users").child(userId).child("galleries").child(key).setValue(gallery)
            updateImage(gallery)
        }
    }

    override suspend fun update(activity: ActivityModel) {
        var foundActivity: ActivityModel? = activities.find { p -> p.fbId == activity.fbId }
        if (foundActivity != null) {
            foundActivity.description = activity.description
            foundActivity.category = activity.category
            foundActivity.rating = activity.rating
            foundActivity.title = activity.title
            foundActivity.priority = activity.priority
            foundActivity.hour = activity.hour
            foundActivity.minute = activity.minute
            foundActivity.date = activity.date
            foundActivity.selectedTime = activity.selectedTime
            foundActivity.selectedDate = activity.selectedDate
        }
        db.child("users").child(userId).child("activities").child(activity.fbId).setValue(activity)

    }

    override suspend fun updateGallery(gallery: ActivityGalleryModel) {
        var foundGalleryActivity: ActivityGalleryModel? = galleries.find { p -> p.fbGalleryId == gallery.fbGalleryId }
        if (foundGalleryActivity != null) {
            foundGalleryActivity.image = gallery.image
            foundGalleryActivity.imagetitle = gallery.imagetitle
            foundGalleryActivity.imagecategory = gallery.imagecategory
            foundGalleryActivity.imagedescription = gallery.imagedescription
            foundGalleryActivity.favourite = gallery.favourite
        }
        db.child("users").child(userId).child("galleries").child(gallery.fbGalleryId).setValue(gallery)
        if(gallery.image.isNotEmpty()){
            updateImage(gallery)
    }}

    override suspend fun delete(activity: ActivityModel) {
        db.child("users").child(userId).child("activities").child(activity.fbId).removeValue()
        activities.remove(activity)
    }

    override suspend fun deleteGallery(gallery: ActivityGalleryModel) {
        db.child("users").child(userId).child("galleries").child(gallery.fbGalleryId).removeValue()
        galleries.remove(gallery)
    }

    override suspend fun clear() {
        activities.clear()
    }

    override suspend fun clearGallery() {
        galleries.clear()
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

    fun fetchGalleries(galleriesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(galleries) {
                    it.getValue<ActivityGalleryModel>(
                        ActivityGalleryModel::class.java
                    )
                }
                galleriesReady()
            }
        }

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        galleries.clear()
        db.child("users").child(userId).child("galleries")
            .addListenerForSingleValueEvent(valueEventListener)

    }

    fun updateImage(gallery: ActivityGalleryModel) {
        if (gallery.image != "") {
            val fileName = File(gallery.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, gallery.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        gallery.image = it.toString()
                        db.child("users").child(userId).child("galleries").child(gallery.fbGalleryId).setValue(gallery)
                    }
                }
            }
        }
    }

}