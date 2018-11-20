package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import remi.scoreboard.AbsentLiveData
import remi.scoreboard.data.Player
import remi.scoreboard.data.PlayerList
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
                // TODO setter in model
                dbUser?.let { dbUser ->
                    dbUser.username = user.username
                    dbUser.email = user.email
                    dbUser.avatar = user.avatar
                }
            }
        }

    fun deleteAllPlayerOfUser(userId: String) =
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val user = realm.where(User::class.java).equalTo("id", userId).findFirst()
                user?.playerList = PlayerList()
            }
        }

    fun addPlayerToUser(player: Player, userId: String) =
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val user = realm.where(User::class.java).equalTo("id", userId).findFirst()
                user?.playerList?.add(player)
            }
        }

    fun deletePlayerOfUser(playerId: String, userId: String) =
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val user = realm.where(User::class.java).equalTo("id", userId).findFirst()
                user?.playerList?.removeIf { it.id == playerId }
            }
        }

    fun renamePlayerOfUser(playerId: String, newPlayerName: String, userId: String?) =
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val user = realm.where(User::class.java).equalTo("id", userId).findFirst()
                user?.playerList?.find { player -> player.id == playerId }?.username = newPlayerName
            }
        }
}
