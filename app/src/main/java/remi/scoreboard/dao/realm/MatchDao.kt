package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import remi.scoreboard.data.Match
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.util.AbsentLiveData

object MatchDao {
    // READ
    fun loadAll(): LiveData<List<Match>> =
        RealmManager.instance.run { where(Match::class.java).findAll().asLiveData() }

    fun loadGameWithId(id: String): LiveData<Match> =
        RealmManager.instance.run {
            val match = where(Match::class.java).equalTo("id", id).findFirst()
            if (match == null)
                AbsentLiveData.create()
            else
                LiveRealmObject(match)
        }

    fun getUnmanagedMatchById(id: String): Match =
        Realm.getDefaultInstance().use {
            val match = it.where(Match::class.java).equalTo("id", id).findFirst()
            it.copyFromRealm(match!!)
        }

    // WRITE
    fun insert(match: Match) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insert(match) } }

    fun insertOrUpdate(match: Match) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insertOrUpdate(match) } }

    fun insertOrUpdate(matchList: List<Match>) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insertOrUpdate(matchList) } }

    fun deleteAll() =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.delete(Match::class.java) } }

    fun addPlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        Realm.getDefaultInstance().use {
            it.executeTransaction {
                currentMatch.value?.scorePlayerList?.add(
                    playerScore
                )
            }
        }

    fun removePlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        Realm.getDefaultInstance().use {
            it.executeTransaction {
                currentMatch.value?.scorePlayerList?.remove(
                    playerScore
                )
            }
        }

    fun deleteMatchId(id: String) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                realm.where(Match::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
        }
    }
}
