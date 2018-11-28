package remi.scoreboard.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ItemFilterListener
import com.mikepenz.fastadapter.select.SelectExtension
import com.wajahatkarim3.easyflipview.EasyFlipView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import remi.scoreboard.R
import remi.scoreboard.data.Game
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentChoosePlayerBinding
import remi.scoreboard.fastadapter.item.ChoosePlayerItem
import remi.scoreboard.viewmodel.ChoosePlayerViewModel

class ChoosePlayerFragment : Fragment() {

    private lateinit var choosePlayerViewModel: ChoosePlayerViewModel
    private lateinit var binding: FragmentChoosePlayerBinding
    private lateinit var fastAdapter: FastItemAdapter<ChoosePlayerItem>

    private var selectionSaveBundle: Bundle? = null
    private var selectExtension: SelectExtension<ChoosePlayerItem>? = null
    private lateinit var currentGame: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        choosePlayerViewModel = ViewModelProviders.of(this).get(ChoosePlayerViewModel::class.java)
        choosePlayerViewModel.updateUserState.observe(this, Observer {
            if (it.status != Status.LOADING)
                binding.swipeRefresh.isRefreshing = false
            if (it.status == Status.ERROR)
                view?.let { view ->
                    if (it.message.isNotEmpty())
                        Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                }
        })
        choosePlayerViewModel.createMatchState.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                val action = ChoosePlayerFragmentDirections.actionStartPlaying(it.message)
                findNavController().navigate(action)
                activity?.finish()
            }
        })

        choosePlayerViewModel.currentUser.observe(this, Observer { user ->
            binding.playerListIsEmpty = user.playerList.isEmpty()
            activity?.invalidateOptionsMenu()
            fastAdapter.setNewList(user.playerList.map { ChoosePlayerItem(it) })

            restoreAdapterSelectionListFromBundleIfNeeded() // restore in case of background/foreground (pause/resume)

            savedInstanceState?.let {
                // restore in case of destroy (rotation)
                fastAdapter.withSavedInstanceState(it)
            }
        })

        activity?.let {
            choosePlayerViewModel.getGameById(ChoosePlayerFragmentArgs.fromBundle(it.intent.extras).gameId)
                .observe(this, Observer { game ->
                    currentGame = game
                })
        }

        fastAdapter = getFastAdapter()
        selectExtension = fastAdapter.getExtension(SelectExtension::class.java)
    }

    override fun onResume() {
        super.onResume()
        choosePlayerViewModel.refreshUser()
    }

    private fun getFastAdapter(): FastItemAdapter<ChoosePlayerItem> {
        val adapter: FastItemAdapter<ChoosePlayerItem> = FastItemAdapter()
        adapter.withSelectable(true)
        adapter.withMultiSelect(true)
        adapter.setHasStableIds(true)
        adapter.withOnClickListener { v, _, _, _ ->
            activity?.invalidateOptionsMenu()
            v?.findViewById<EasyFlipView>(R.id.avatar_flipview)?.flipTheView()
            true
        }
        adapter.itemFilter.withFilterPredicate { item, constraint ->
            runBlocking {
                async(Dispatchers.Main) {
                    // Need to run on UI thread to access player (live realm object)
                    item.player.username.contains(constraint ?: "", true)
                }.await()
            }
        }
        adapter.itemFilter.withItemFilterListener(object : ItemFilterListener<ChoosePlayerItem> {
            override fun onReset() {
                binding.searchIsEmpty = false
            }

            override fun itemsFiltered(constraint: CharSequence?, results: MutableList<ChoosePlayerItem>?) {
                results?.also {
                    binding.searchIsEmpty = it.size == 0
                    if (it.size == 0) {
                        val builder = SpannableStringBuilder()
                        val txt = getString(R.string.player_empty_search_text)
                        builder.append(txt)
                        val txtSpan = SpannableString(constraint)
                        txtSpan.setSpan(StyleSpan(Typeface.BOLD), 0, txtSpan.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        builder.append(txtSpan)
                        binding.includedEmptySearchView.emptySearchText.setText(builder, TextView.BufferType.SPANNABLE)
                    }
                }
            }
        })
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoosePlayerBinding.inflate(inflater, container, false)
        binding.managePlayerListener = View.OnClickListener { startManagePlayerFragment() }
        binding.recycler.adapter = fastAdapter
        // https://stackoverflow.com/a/34012893/9994620
//        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val topRowVerticalPosition =
//                    if (recyclerView.childCount == 0)
//                        0
//                    else
//                        recyclerView.getChildAt(0).top
//                binding.swipeRefresh.isEnabled = topRowVerticalPosition >= 0
//            }
//        })
        binding.swipeRefresh.setOnRefreshListener {
            saveAdapterSelectionListToBundle()
            choosePlayerViewModel.refreshUser()
        }
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_choose_player, menu)
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

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        if (binding.playerListIsEmpty == true)
            menu?.removeItem(R.id.action_search)
        selectExtension?.selectedItems?.let {
            if (it.size < 2) {
                menu?.removeItem(R.id.action_start_playing)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_manage_player -> {
                startManagePlayerFragment()
                true
            }
            R.id.action_start_playing -> {
                createMatchAndStartPlayActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        saveAdapterSelectionListToBundle()
    }

    private fun saveAdapterSelectionListToBundle() {
        selectionSaveBundle = Bundle()
        fastAdapter.saveInstanceState(selectionSaveBundle)
    }

    private fun restoreAdapterSelectionListFromBundleIfNeeded() {
        selectionSaveBundle?.let {
            fastAdapter.withSavedInstanceState(it)
            selectionSaveBundle = null // Consume bundle
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fastAdapter.saveInstanceState(outState)
    }

    private fun startManagePlayerFragment() {
        val action = ChoosePlayerFragmentDirections.actionManagePlayers()
        findNavController().navigate(action)
    }

    private fun createMatchAndStartPlayActivity() {
        val playerList = selectExtension?.selectedItems?.map { it.player }
        if (playerList != null && ::currentGame.isInitialized)
            choosePlayerViewModel.createMatch(currentGame, playerList)
    }
}
