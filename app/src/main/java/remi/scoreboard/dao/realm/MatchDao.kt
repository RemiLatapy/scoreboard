package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.exceptions.RealmException
import remi.scoreboard.data.Match
import remi.scoreboard.data.PlayerScore

object MatchDao {
    // READ
    fun loadAll(): LiveData<List<Match>> =
        RealmManager.instance.run { where(Match::class.java).findAll().asLiveData() }

    /**
     * @throws RealmException no match found
     */
    fun loadGameWithId(id: String): LiveData<Match> =
        RealmManager.instance.run {
            val match = where(Match::class.java).equalTo("id", id).findFirst()
                ?: throw RealmException("No match object found with ID $id")

            LiveRealmObject(match)
        }

    // WRITE
    fun insert(match: Match) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insert(match) } }

    fun insertOrUpdate(match: Match) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insertOrUpdate(match) } }

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

    fun addPoints(playerScore: PlayerScore, points: Int) =
        Realm.getDefaultInstance().use { it.executeTransaction { playerScore.score += points } }


}
