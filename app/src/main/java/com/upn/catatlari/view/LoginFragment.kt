package com.upn.catatlari.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.upn.catatlari.databinding.FragmentLoginBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.upn.catatlari.data.local.database.AppDatabase

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)

        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBinding.buttonLogin.setOnClickListener {

            val emailUser = loginBinding.etEmail.text.toString()
            val passwordUser = loginBinding.etPassword.text.toString()

            if (emailUser.isEmpty() || passwordUser.isEmpty()) {
                Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(requireContext())
            val userDao = db.userDao()

            lifecycleScope.launch {
                val user = userDao.login(emailUser, passwordUser)

                if (user != null) {
                    Toast.makeText(requireContext(), "Login berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    val userModel = com.upn.catatlari.model.User(
                        id = user.id,
                        email = user.username,
                        password = user.password
                    )

                    intent.putExtra("user", userModel)
                    startActivity(intent)

                } else {
                    Toast.makeText(requireContext(), "Email atau password salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}