package ie.wit.happybaby.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.wit.happybaby.helpers.Converters
import ie.wit.happybaby.models.ActivityGalleryModel
import ie.wit.happybaby.models.ActivityModel

@Database(entities = arrayOf(ActivityModel::class, ActivityGalleryModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun activityDao(): ActivityDao
}

