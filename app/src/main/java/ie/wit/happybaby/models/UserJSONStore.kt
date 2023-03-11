package ie.wit.happybaby.models

import android.content.Context
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.happybaby.helpers.*
import timber.log.Timber.i
import java.lang.reflect.Type
import java.util.*

const val USER_JSON_FILE = "users.json"
val userGsonBuilder: Gson = GsonBuilder().setPrettyPrinting().create()
val userListType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateUserId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    var users = mutableListOf<UserModel>()

    init {
        if (exists(context, USER_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAllUsers(): MutableList<UserModel> {
        logAll()
        return users
    }

    override fun createUser(user: UserModel) {
        user.id = generateUserId()
        users.add(user)
        serialize()
        logAll()
    }

    override fun login(username: String, password: String): Boolean {
        var foundUser: UserModel? = users.find { t -> t.username == username }
        if (foundUser != null) {
            return foundUser.password == password
        }
        return false
    }

    private fun serialize() {
        val jsonString = userGsonBuilder.toJson(users, userListType)
        write(context, USER_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, USER_JSON_FILE)
        users = userGsonBuilder.fromJson(jsonString, userListType)
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    private fun logAll() {
        users.forEach { i("$it") }
    }
}


