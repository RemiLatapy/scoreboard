package remi.scoreboard.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import remi.scoreboard.R
import remi.scoreboard.activity.LoginSignupActivity
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentUserBinding
import remi.scoreboard.viewmodel.UserViewModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel.updateUserState.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = it.status == Status.LOADING
            if (it.status == Status.ERROR)
                view?.let { view ->
                    if (it.message.isNotEmpty())
                        Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                }
        })
        viewModel.logOutState.observe(this, Observer { cb ->
            when (cb.status) {
                Status.SUCCESS -> {
                    activity?.run {
                        startActivity(Intent(this, LoginSignupActivity::class.java))
                        finish()
                    }
                }
                Status.ERROR -> Toast.makeText(context, "Sign out failed: ${cb.message}", Toast.LENGTH_SHORT).show()
                else -> {
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.signoutListener = signoutListener
        binding.loginListener = loginListener
        binding.managePlayersListener = managePlayersListener
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshUser()
        }
        binding.setLifecycleOwner(this)
        return binding.root
    }

    private val signoutListener = View.OnClickListener { viewModel.logOut() }

    private val loginListener = View.OnClickListener {
        val action = UserFragmentDirections.actionLoginSignup()
        findNavController().navigate(action)
    }

    private val managePlayersListener = View.OnClickListener {
        val action = UserFragmentDirections.actionManagePlayers()
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_edit_user -> {
                findNavController().navigate(UserFragmentDirections.actionEditUser())
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}

