package ie.wit.happybaby.helpers



import ie.wit.happybaby.models.ActivityModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


fun exportToCSV(filename: String, path: File?, header: String, activities: MutableList<ActivityModel> ): File {
    val fileOut = File(path, filename)

    CoroutineScope(Dispatchers.IO).launch {
        runCatching {
            FileOutputStream(fileOut).use {
                it.write(header.toByteArray())
                for (activity in activities) {
                    it.write("${activity.selectedDate},${activity.selectedTime},${activity.category},${activity.description},${activity.rating}\n".toByteArray())
                }
            }
        }
    }
    return fileOut
}