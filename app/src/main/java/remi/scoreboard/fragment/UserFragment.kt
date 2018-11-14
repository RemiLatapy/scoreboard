package remi.scoreboard.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import remi.scoreboard.activity.LoginSignupActivity
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentUserBinding
import remi.scoreboard.viewmodel.UserViewModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserViewModel
    private var userId = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel.userId.observe(this, Observer { userId = it })
        viewModel.signOutState.observe(this, Observer { cb ->
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
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.signoutListener = createSignoutListener()
        binding.loginListener = createLoginListener()
        binding.managePlayersListener = createManagePlayersListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Do it here to be updated when coming back from ManagePlayersActivity
        viewModel.playerList.observe(this, Observer {
            binding.playerList = it
        })
    }

    private fun createSignoutListener() = View.OnClickListener { viewModel.signOut() }

    private fun createLoginListener() = View.OnClickListener {
        val action = UserFragmentDirections.actionLoginSignup()
        findNavController().navigate(action)
    }

    private fun createManagePlayersListener() = View.OnClickListener {
        val action = UserFragmentDirections.actionManagePlayers(userId)
        findNavController().navigate(action)
    }
}
