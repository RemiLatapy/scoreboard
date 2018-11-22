package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback
import kotlinx.android.synthetic.main.dialog_add_score.view.*
import remi.scoreboard.R
import remi.scoreboard.databinding.FragmentGamePlayBinding
import remi.scoreboard.fastadapter.item.PlayerScoreItem
import remi.scoreboard.viewmodel.GamePlayViewModel
import java.util.*


class GamePlayFragment : Fragment() {

    private lateinit var viewmodel: GamePlayViewModel
    private lateinit var binding: FragmentGamePlayBinding
    private lateinit var fastAdapter: FastItemAdapter<PlayerScoreItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.let { activity ->
            val matchId = GamePlayFragmentArgs.fromBundle(activity.intent.extras).matchId
            viewmodel = ViewModelProviders.of(this, GamePlayViewModel.GamePlayViewModelFactory(matchId))
                .get(GamePlayViewModel::class.java)
            viewmodel.currentMatch.observe(this, Observer { match ->
                fastAdapter.setNewList(match.scorePlayerList
                    .map { playerScore -> PlayerScoreItem(playerScore) }
                    .sortedBy { it.playerScore.number })
            })
        }

        fastAdapter = getFastAdapter()
    }

    private fun getFastAdapter(): FastItemAdapter<PlayerScoreItem> =
        FastItemAdapter<PlayerScoreItem>().apply {
            setHasStableIds(true)
            withOnClickListener { _, _, item, _ ->
                showAddPointsDialog(item)
                true
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGamePlayBinding.inflate(inflater, container, false)
        binding.recycler.adapter = fastAdapter

        val touchHelper = ItemTouchHelper(SimpleDragCallback(object : ItemTouchCallback {
            override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
                Collections.swap(fastAdapter.adapterItems, oldPosition, newPosition)
                return true
            }

            override fun itemTouchDropped(oldPosition: Int, newPosition: Int) {
                viewmodel.updatePlayerScoreList(fastAdapter.adapterItems.map { it.playerScore })
            }

        }))
        touchHelper.attachToRecyclerView(binding.recycler)

        binding.finishGameListener = View.OnClickListener { activity?.finish() }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gameplay, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_finish_game -> {
                activity?.finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showAddPointsDialog(playerScoreItem: PlayerScoreItem) {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.let {
            val view = layoutInflater.inflate(R.layout.dialog_add_score, null)
            it.setTitle("Add points to ${playerScoreItem.playerScore.player?.username}")
                .setView(view)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    viewmodel.addPoints(playerScoreItem.playerScore, view.txt_points.text.toString().toInt())
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}