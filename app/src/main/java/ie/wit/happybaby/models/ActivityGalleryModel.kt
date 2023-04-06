package ie.wit.happybaby.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ActivityGalleryModel(@PrimaryKey(autoGenerate = true)
                         var galleryid: Long = 0,
                                var fbGalleryId: String = "",
                                var image: String = "",
                                var favourite: Boolean = false,
                                var imagetitle: String = "",
                                var imagecategory: String = "N/A",
                                var imagedescription: String = "") : Parcelable


