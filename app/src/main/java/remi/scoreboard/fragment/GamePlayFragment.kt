package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback
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

        (activity as? AppCompatActivity)?.let { activity ->
            activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            viewmodel = ViewModelProviders.of(activity).get(GamePlayViewModel::class.java)
            viewmodel.currentPlayerScoreList.observe(this, Observer { playerScoreList ->
                fastAdapter.setNewList(playerScoreList
                    .map { playerScore -> PlayerScoreItem(playerScore) }
                    .sortedBy { it.playerScore.order })
            })
            viewmodel.saveLocalMatchState.observe(this, Observer {
                if (it.status == Status.SUCCESS)
                    viewmodel.deleteLocalMatch()
                if (it.status == Status.ERROR)
                    view?.let { view ->
                        if (it.message.isNotEmpty())
                            Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                    }
            })
            viewmodel.deleteLocalMatchState.observe(this, Observer {
                if (it.status == Status.SUCCESS) {
                    activity.finish()
                } else if (it.status == Status.ERROR)
                    view?.let { view ->
                        if (it.message.isNotEmpty())
                            Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                    }
            })

            fastAdapter = getFastAdapter()
        }
    }

    private fun getFastAdapter(): FastItemAdapter<PlayerScoreItem> =
        FastItemAdapter<PlayerScoreItem>().apply {
            setHasStableIds(true)
            withOnClickListener { _, _, item, _ ->
                viewmodel.showAddPointsDialog(activity, item.playerScore)
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
        binding.finishGameListener = View.OnClickListener { viewmodel.showConfirmFinishDialog(activity) }
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gameplay, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                viewmodel.showConfirmExitDialog(activity)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}