package remi.scoreboard.fragment

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentStatsBinding
import remi.scoreboard.fastadapter.item.MatchItem
import remi.scoreboard.viewmodel.MatchViewModel

class StatsFragment : Fragment() {

    private lateinit var fastAdapter: FastItemAdapter<MatchItem>
    private lateinit var binding: FragmentStatsBinding
    private lateinit var viewModel: MatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MatchViewModel::class.java)

        viewModel.refreshMatchListState.observe(this, Observer {
            if (it.status != Status.LOADING)
                binding.swipeRefresh.isRefreshing = false
            if (it.status == Status.ERROR)
                view?.let { view ->
                    if (it.message.isNotEmpty())
                        Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                }
        })

        viewModel.allMatchs.observe(this, Observer { matchList ->
            binding.displayEmptyView = matchList.isEmpty()
            matchList?.let {
                fastAdapter.setNewList(it.map { match ->
                    MatchItem(match).apply {
                        formatedDate = DateFormat.getDateFormat(context).format(match.date)
                    }
                })
            }
        })

        fastAdapter = getFastAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMatchList()
    }

    private fun getFastAdapter(): FastItemAdapter<MatchItem> =
        FastItemAdapter<MatchItem>().apply {
            setHasStableIds(true)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        binding.recycler.adapter = fastAdapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshMatchList() }
        return binding.root
    }


}
