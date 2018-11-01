package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import remi.scoreboard.data.*
import remi.scoreboard.repository.MatchRepository
import java.util.*

class SharedViewModel : ViewModel() {

    private val matchRepository = MatchRepository()

    lateinit var currentMatch: LiveData<Match>

    private fun addUser(user: User) {
        matchRepository.addPlayer(currentMatch, PlayerScore(user))
    }

    fun toogleUser(user: User) {
        val player = currentMatch.value?.scorePlayerList?.find { it.user == user }
        if(player != null)
            matchRepository.removePlayer(currentMatch, player)
        else
            addUser(user)
    }

    fun createMatch(game: Game): LiveData<Match> {
        currentMatch = matchRepository.create(Match(game, PlayerScoreList(), Date()))
        return currentMatch
    }
}