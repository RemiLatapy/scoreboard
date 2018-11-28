package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.exceptions.RealmException
import remi.scoreboard.data.PlayerScore

object PlayerScoreDao {

    // READ
    fun loadPlayerScoreWithIdStartingBy(idStarting: String): LiveData<List<PlayerScore>> =
        RealmManager.instance.run { where(PlayerScore::class.java).contains("id", idStarting).findAll().asLiveData() }

    // WRITE
    fun setScore(playerScore: PlayerScore, score: Int) =
        Realm.getDefaultInstance().use { it.executeTransaction { playerScore.score = score } }

    fun updatePlayerScore(playerScoreList: List<PlayerScore>) = Realm.getDefaultInstance().use {
        it.executeTransaction { realm ->
            realm.insertOrUpdate(playerScoreList)
        }
    }

    fun setOrder(playerScoreList: List<PlayerScore>, orderList: List<Int>) {
        if (playerScoreList.size != orderList.size)
            throw RealmException("Player list size and order list size are different")
        Realm.getDefaultInstance().use {
            it.executeTransaction {
                playerScoreList.forEachIndexed { idx, playerScore ->
                    playerScore.order = orderList[idx]
                }
            }
        }
    }
}
