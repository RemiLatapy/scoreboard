package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import remi.scoreboard.R
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentGameListBinding
import remi.scoreboard.fastadapter.item.GameItem
import remi.scoreboard.viewmodel.GameViewModel

class GameListFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentGameListBinding
    private lateinit var fastAdapter: FastItemAdapter<GameItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        fastAdapter = FastItemAdapter()
        fastAdapter.setHasStableIds(true)
        fastAdapter.itemFilter.withFilterPredicate { item, constraint ->
            runBlocking {
                async(Dispatchers.Main) {
                    // Need to run on UI thread to access game (live realm object)
                    item.game.name.contains(constraint ?: "", true)
                }.await()
            }
        }
        fastAdapter.withOnClickListener { _, _, gameItem, _ ->
            val action = GameListFragmentDirections.actionChoosePlayers(gameItem.game.id)
            findNavController().navigate(action)
            true
        }

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        gameViewModel.updateGameListState.observe(this, Observer {
            if (it.status != Status.LOADING)
                binding.swipeRefresh.isRefreshing = false
            if (it.status == Status.ERROR)
                view?.let { view ->
                    if (it.message.isNotEmpty())
                        Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                }
        })

        gameViewModel.allGames.observe(this,
            Observer { gameList ->
                gameList?.let {
                    fastAdapter.setNewList(it.map { game -> GameItem(game) })
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameListBinding.inflate(inflater, container, false)

        binding.recycler.adapter = fastAdapter
        // https://stackoverflow.com/a/34012893/9994620
        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val topRowVerticalPosition =
                    if (recyclerView.childCount == 0)
                        0
                    else
                        recyclerView.getChildAt(0).top
                binding.swipeRefresh.isEnabled = topRowVerticalPosition >= 0
            }
        })
        binding.swipeRefresh.setOnRefreshListener { gameViewModel.updateGameList() }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_game, menu)
        (menu?.findItem(R.id.action_search)?.actionView as? SearchView)?.setOnQueryTextListener(queryTextListener)
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            fastAdapter.filter(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            fastAdapter.filter(newText)
            return true
        }
    }
}

