package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback
import kotlinx.android.synthetic.main.dialog_add_score.view.*
import remi.scoreboard.R
import remi.scoreboard.data.Status
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

        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewmodel = ViewModelProviders.of(this).get(GamePlayViewModel::class.java)
        viewmodel.currentPlayerScoreList.observe(this, Observer { playerScoreList ->
            fastAdapter.setNewList(playerScoreList
                .map { playerScore -> PlayerScoreItem(playerScore) }
                .sortedBy { it.playerScore.order })
        })
        viewmodel.saveLocalMatchState.observe(this, Observer {
            if (it.status == Status.SUCCESS)
                activity?.finish()
            else if (it.status == Status.ERROR)
                view?.let { view ->
                    if (it.message.isNotEmpty())
                        Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                }
        })

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
                viewmodel.reorderPlayerScoreList(fastAdapter.adapterItems.map { it.playerScore })
            }

        }))
        touchHelper.attachToRecyclerView(binding.recycler)

        binding.viewmodel = viewmodel
        binding.finishGameListener = View.OnClickListener { activity?.finish() }
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gameplay, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                showConfirmExitDialog()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmExitDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Exit")
                setMessage("Discard or finish game?")
                setPositiveButton("finish") { _, _ -> viewmodel.saveAndDeleteLocalMatch() }
                setNeutralButton("cancel", null)
                setNegativeButton("discard") { _, _ ->
                    viewmodel.deleteLocalMatch()
                    it.finish() // TODO verify call order and risk
                }
                show()
            }
        }
    }

    private fun showAddPointsDialog(playerScoreItem: PlayerScoreItem) {
        activity?.let {
            AlertDialog.Builder(it).apply {
                val view = layoutInflater.inflate(R.layout.dialog_add_score, null)
                setTitle("Add points to ${playerScoreItem.playerScore.player?.username}")
                setView(view)
                setPositiveButton(
                    "Add"
                ) { _, _ ->
                    viewmodel.addPoints(playerScoreItem.playerScore, view.txt_points.text.toString().toInt())
                }
                setNegativeButton("Cancel", null)
                show()
            }
        }
    }
}