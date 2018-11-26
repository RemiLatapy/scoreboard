package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.exceptions.RealmException
import remi.scoreboard.data.User
import remi.scoreboard.util.AbsentLiveData

object UserDao {
    // READ
    fun load(userId: String): LiveData<User> = RealmManager.instance.run {
        val user = where(User::class.java).equalTo("id", userId).findFirst()
        return if (user == null)
            AbsentLiveData.create()
        else
            LiveRealmObject(user)
    }

    /**
     * @throws RealmException No user found with id [userId]
     */
    fun load2(userId: String): LiveData<User> = RealmManager.instance.run {
        val user = where(User::class.java).equalTo("id", userId).findFirst()
        if (user == null)
            throw RealmException("No user found with id $userId")
        LiveRealmObject(user)
    }

    // WRITE
    fun insert(user: User) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insert(user) } }

    fun insertOrUpdate(user: User) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insertOrUpdate(user) } }

    // TODO call this in login activity after logout
    fun deleteUser() {
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                realm.delete(User::class.java)
            }
        }
    }
}
