package remi.scoreboard.fragment


import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import remi.scoreboard.R
import remi.scoreboard.data.Status
import remi.scoreboard.viewmodel.LoginSignupViewModel


// TODO check connection state
class LoginFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var textInputLayoutUsername: TextInputLayout
    private lateinit var textInputLayoutPwd: TextInputLayout
    private lateinit var textUsername: EditText
    private lateinit var textPwd: EditText
    private lateinit var resetPasswordDialog: AlertDialog

    private lateinit var viewModel: LoginSignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(LoginSignupViewModel::class.java)

        viewModel.loginState.observe(this, Observer { cb ->
            when (cb.status) {
                Status.SUCCESS -> {
                    viewModel.updateGameListState.observe(this, Observer { gameCb ->
                        when (gameCb.status) {
                            Status.SUCCESS -> {
                                progressBar.visibility = View.INVISIBLE
                                val action = LoginFragmentDirections.ActionLoginToMain()
                                findNavController().navigate(action)
                                activity?.finish()
                            }
                            Status.ERROR -> {
                                progressBar.visibility = View.INVISIBLE
                                Snackbar.make(view!!, cb.message, Snackbar.LENGTH_LONG).show()
                            }
                            else -> {
                            }
                        }
                    })
                    viewModel.updateGameList()
                }
                Status.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    Snackbar.make(view!!, cb.message, Snackbar.LENGTH_LONG).show()
                    // TODO handle different errors
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.IDLE -> {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.resetPasswordState.observe(this, Observer { cb ->
            when (cb.status) {
                Status.SUCCESS -> {
                    Snackbar.make(view!!, cb.message, Snackbar.LENGTH_LONG).show()
                    resetPasswordDialog.dismiss()
                }
                Status.ERROR -> {
                    Snackbar.make(view!!, cb.message, Snackbar.LENGTH_LONG).show()
                    // TODO handle different errors
                }
                Status.LOADING -> {
                }
                Status.IDLE -> {
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViews(view) // this need to be done first (find some views)
        setupLogging(view)
        setupCreateAccount(view)
        setupSkip(view)
        setupResetPassword(view)
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
                val password = findViewById<EditText>(R.id.password).text.toString()
                viewModel.loginUser(username, password)
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

    private fun setupResetPassword(view: View) {
        AlertDialog.Builder(view.rootView.context).let { builder ->
            val inflater = LayoutInflater.from(view.rootView.context)
            val dialogView = inflater.inflate(R.layout.dialog_forgot_password, null)
            resetPasswordDialog = builder.setTitle("Reset password")
                .setView(dialogView)
                .setPositiveButton("Reset", null)
                .setNegativeButton("Cancel", null)
                .create()

            resetPasswordDialog.setOnShowListener {
                resetPasswordDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val emailLayout = dialogView.findViewById<TextInputLayout>(R.id.textInputLayoutEmail)
                    val email = emailLayout.findViewById<TextInputEditText>(R.id.email)
                    email.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) emailLayout.error = null }
                    viewModel.resetPassword(email.text.toString())
                }
            }
        }

        val resetPasswordTextView = view.findViewById<TextView>(R.id.link_forgot_pwd)
        resetPasswordTextView.paintFlags = resetPasswordTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        resetPasswordTextView.setOnClickListener {
            resetPasswordDialog.show()
        }
    }
}
