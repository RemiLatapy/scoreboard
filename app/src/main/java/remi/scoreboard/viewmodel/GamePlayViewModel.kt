package remi.scoreboard.viewmodel

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.dialog_add_score.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.R
import remi.scoreboard.activity.MainActivity
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.repository.MatchRepository
import remi.scoreboard.repository.PlayerScoreRepository
import kotlin.coroutines.CoroutineContext

class GamePlayViewModel : ViewModel() {
    private val playerScoreRepository = PlayerScoreRepository()
    private val matchRepository = MatchRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    val currentPlayerScoreList: LiveData<List<PlayerScore>> = playerScoreRepository.tempPlayerScoreList

    val deleteLocalMatchState = matchRepository.deleteLocalMatchState
    val saveLocalMatchState = matchRepository.saveLocalMatchState
    val saveLocalMatchStateStatus =
        Transformations.map(matchRepository.saveLocalMatchState) { it.status }!! // TODO null check

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun reorderPlayerScoreList(playerScoreList: List<PlayerScore>) {
        scope.launch(Dispatchers.Main) {
            playerScoreRepository.setLocalOrder(
                playerScoreList,
                playerScoreList.mapIndexed { idx, _ -> idx })
        }
    }

    fun addPoints(playerScore: PlayerScore, points: Int) {
        scope.launch(Dispatchers.Main) {
            playerScoreRepository.setLocalScore(playerScore, playerScore.score + points)
        }
    }

    fun deleteLocalMatch() {
        scope.launch(Dispatchers.Main) {
            matchRepository.deleteLocalMatch()
        }
    }

    fun saveLocalMatch() {
        scope.launch(Dispatchers.IO) {
            matchRepository.saveLocalMatch()
        }
    }

    fun showConfirmExitDialog(activity: Activity?) {
        activity?.let { act ->
            AlertDialog.Builder(act).apply {
                setTitle(act.getString(R.string.exit))
                setMessage(act.getString(R.string.discard_finish_dialog_message))
                setPositiveButton(act.getString(R.string.finish)) { _, _ ->
                    MainActivity.fragmentDest = R.id.stats_dest
                    saveLocalMatch()
                }
                setNeutralButton(act.getString(R.string.cancel), null)
                setNegativeButton(act.getString(R.string.discard)) { _, _ ->
                    MainActivity.fragmentDest = R.id.game_list_dest
                    deleteLocalMatch()
                }
                show()
            }
        }
    }

    fun showConfirmFinishDialog(activity: Activity?) {
        activity?.let { act ->
            AlertDialog.Builder(act).apply {
                setTitle(act.getString(R.string.finish_game_dialog_title))
                setMessage(act.getString(R.string.finish_game_dialog_message))
                setPositiveButton(act.getString(R.string.finish)) { _, _ ->
                    MainActivity.fragmentDest = R.id.stats_dest
                    saveLocalMatch() }
                setNegativeButton(act.getString(R.string.cancel), null)
                show()
            }
        }
    }

    fun showAddPointsDialog(activity: Activity?, playerScore: PlayerScore) {
        activity?.let {act ->
            AlertDialog.Builder(act).apply {
                val view = act.layoutInflater.inflate(R.layout.dialog_add_score, null)
                setTitle("Add points to ${playerScore.player?.username}")
                setView(view)
                setPositiveButton(
                    "Add"
                ) { _, _ ->
                    addPoints(playerScore, view.txt_points.text.toString().toInt())
                }
                setNegativeButton("Cancel", null)
                show()
            }
        }
    }

//    class GamePlayViewModelFactory(val matchId: String) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return GamePlayViewModel(matchId) as T
//        }
//    }
}



