package com.upn.catatlari.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.upn.catatlari.databinding.FragmentProfileBinding
import com.upn.catatlari.R
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import com.upn.catatlari.data.local.database.AppDatabase
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener { requireActivity().finish() }

        // Edit profile
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val db = AppDatabase.getDatabase(requireContext())
        val userDao = db.userDao()

        lifecycleScope.launch {
            val user = userDao.getLastUser()

            if (user != null) {
                binding.tvName.text = user.nama
                binding.tvEmail.text = user.username
            }
        }
    }

}