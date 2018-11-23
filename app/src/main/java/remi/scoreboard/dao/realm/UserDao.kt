package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import remi.scoreboard.util.AbsentLiveData
import remi.scoreboard.data.User

object UserDao {
    // READ
    fun load(userId: String): LiveData<User> = RealmManager.instance.run {
        val user = where(User::class.java).equalTo("id", userId).findFirst()
        return if (user == null)
            AbsentLiveData.create()
        else
            LiveRealmObject(user)
    }

    // WRITE
    fun insert(user: User) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insert(user) } }

    fun insertOrUpdate(user: User) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insertOrUpdate(user) } }

    fun update(user: User) =
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val dbUser = realm.where(User::class.java).equalTo("id", user.id).findFirst()
                dbUser?.set(user)
            }
        }

    // TODO call this in login activity after logout
    fun deleteUser() {
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                realm.delete(User::class.java)
            }
        }
    }
}
