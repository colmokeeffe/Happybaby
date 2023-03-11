/*package ie.wit.happybaby.models

import timber.log.Timber.i

var lastUserId = 0L

internal fun getUserId(): Long {
    return lastUserId++
}

class UserMemStore: UserStore {

    val users = ArrayList<UserModel>()

    override fun findAllUsers(): List<UserModel> {
        return users
    }

    override fun createUser(user: UserModel) {
        user.id = getId()
        users.add(user)
        logAll()
    }

    override fun login(username: String, password: String): Boolean {
        return username.equals("admin") && password.equals("admin")
    }

    override fun delete(user: UserModel) {
        users.remove(user)
    }

    fun logAll() {
        users.forEach{ i("${it}") }
    }

}

 */