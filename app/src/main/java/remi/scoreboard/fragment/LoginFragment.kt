package remi.scoreboard.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseUser
import remi.scoreboard.R

// TODO check connection state
class LoginFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var textInputLayoutUsername: TextInputLayout
    private lateinit var textInputLayoutPwd: TextInputLayout
    private lateinit var textUsername: EditText
    private lateinit var textPwd: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViews(view) // this need to be done first (find some views)
        setupLogging(view)
        setupCreateAccount(view)
        setupSkip(view)
    }

    private fun setupTextViews(view: View) {
        progressBar = view.findViewById(R.id.progress)
        progressBar.visibility = View.INVISIBLE

        textInputLayoutUsername = view.findViewById(R.id.textInputLayoutUsername)
        textInputLayoutPwd = view.findViewById(R.id.textInputLayoutPwd)
        textUsername = view.findViewById(R.id.username)
        textPwd = view.findViewById(R.id.password)

        textUsername.setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let {
                if (!hasFocus) it.setText(it.text.toString().trim()) // remove trailing whitespace due to auto-completion
                else textInputLayoutUsername.error = null; textInputLayoutPwd.error = null
            }
        }
        textPwd.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) textInputLayoutUsername.error = null; textInputLayoutPwd.error = null
        }
    }

    private fun setupLogging(view: View) {
        view.run {
            findViewById<Button>(R.id.login_btn)?.setOnClickListener {
                val username = findViewById<EditText>(R.id.username).text.toString()
                val pwd = findViewById<EditText>(R.id.password).text.toString()
                progressBar.visibility = View.VISIBLE
                ParseUser.logInInBackground(username, pwd) { loggedUser, e ->
                    if (loggedUser != null) {
                        Log.d("LOGIN", "Logging succeed for ${ParseUser.getCurrentUser().username}")
                        val action = LoginFragmentDirections.actionLoginToMain()
                        findNavController().navigate(action)
                        activity?.finish() // Manually finish login activity -- Should use popUpTo but doesn't work as expected...
                    } else {
                        // TODO handle error (UI)
                        Log.d("LOGIN", "Logging failed: ${e.message}")
                        progressBar.visibility = View.INVISIBLE
                        Snackbar.make(view, "Log in failed", Snackbar.LENGTH_SHORT).show()
                        textInputLayoutUsername.error = "Verify username"
                        textInputLayoutPwd.error = "Verify password"
                    }
                }
            }
        }
    }

    private fun setupCreateAccount(view: View) {
        view.run {
            findViewById<TextView>(R.id.link_sign_up)?.setOnClickListener {
                val action = LoginFragmentDirections.actionSignup()
                findNavController().navigate(action)
            }
        }
    }

    private fun setupSkip(view: View) {
        view.findViewById<TextView>(R.id.link_skip_login)?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToMain()
            findNavController().navigate(action)
            activity?.finish() // Manually finish login activity -- Should use popUpTo but doesn't work as expected...
        }
    }
}
