package com.upn.catatlari.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.upn.catatlari.data.local.database.AppDatabase
import com.upn.catatlari.data.local.entity.UserEntity
import com.upn.catatlari.databinding.FragmentEditProfileBinding
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        val userDao = db.userDao()

        // ambil user terakhir
        lifecycleScope.launch {
            val user = userDao.getLastUser()

            if (user != null) {
                binding.etName.setText(user.nama)
                binding.etEmail.setText(user.username)
            }
        }

        binding.etPassword.setText("")

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSave.setOnClickListener {

            val newName = binding.etName.text.toString().trim()
            val newEmail = binding.etEmail.text.toString().trim()
            val newPassword = binding.etPassword.text.toString()

            if (newName.isEmpty()) {
                binding.etName.error = "Nama wajib diisi"
                return@setOnClickListener
            }

            if (newEmail.isEmpty()) {
                binding.etEmail.error = "Email wajib diisi"
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(requireContext())
            val userDao = db.userDao()

            lifecycleScope.launch {

                try {
                    val user = userDao.getLastUser()

                    if (user == null) {
                        Toast.makeText(requireContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val updatedUser = UserEntity(
                        id = user.id,
                        nama = newName,
                        username = newEmail,
                        password = if (newPassword.isNotEmpty()) newPassword else user.password
                    )

                    userDao.updateUser(updatedUser)

                    Toast.makeText(requireContext(), "Profil berhasil diupdate", Toast.LENGTH_SHORT).show()

                    if (isAdded) {
                        parentFragmentManager.popBackStack()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}