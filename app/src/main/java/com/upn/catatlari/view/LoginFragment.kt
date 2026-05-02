package com.upn.catatlari.view

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import com.upn.catatlari.R

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                    Toast.makeText(requireContext(), "Masuk berhasil!", Toast.LENGTH_SHORT).show()

                    // ✅ BIKIN GELANG VIP (SESSION) DI SINI
                    val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("isLoggedIn", true) // Tanda sudah masuk
                        putInt("userId", user.id)      // Simpan ID
                        putString("userName", user.nama) // Simpan Nama
                        apply() // Simpan permanen
                    }

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                } else {
                    Toast.makeText(requireContext(), "Email atau kata sandi salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginBinding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }
}