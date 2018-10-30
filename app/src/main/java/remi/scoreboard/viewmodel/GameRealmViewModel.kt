package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.realmdata.GameRealm
import remi.scoreboard.repository.GameRealmRepository
import kotlin.coroutines.CoroutineContext

class GameRealmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameRealmRepository = GameRealmRepository()
    val allGames: LiveData<List<GameRealm>> = repository.allGames

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(game: GameRealm) = scope.launch(Dispatchers.IO) { repository.insert(game) }

    fun insert(gameList: List<GameRealm>) = scope.launch(Dispatchers.IO) { repository.insert(gameList) }
}