package ie.wit.happybaby.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ActivityModel(@PrimaryKey(autoGenerate = true)
                         var id: Long = 0,
                         var fbId: String = "",
                         var title: String = "",
                         var priority: Boolean = false,
                         var category: String = "N/A",
                         var description: String = "",
                         var selectedTime: String = "",
                         var selectedDate: String = "",
                         var selectedHour: String = "",
                         var selectedMinute: String = "",
                         var date: String="",
                         var hour: Int = 0,
                         var minute: Int = 0,
                         var rating: Float = 0f) : Parcelable



