package com.upn.catatlari.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.upn.catatlari.R
import com.upn.catatlari.data.local.database.AppDatabase
import com.upn.catatlari.databinding.FragmentProfileBinding
import com.upn.catatlari.viewmodel.RunViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var runViewModel: RunViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Logout
        binding.btnLogout.setOnClickListener {
            requireActivity().finish()
        }

        // Edit profile
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ TANPA FACTORY
        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]

        val sharedPref = requireActivity()
            .getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        val userId = sharedPref.getInt("userId", -1)

        if (userId != -1) {
            runViewModel.getRunsByUser(userId)
                .observe(viewLifecycleOwner) { runList ->

                    val totalDistance = runList.sumOf { it.runDistance }

                    val totalSeconds = runList.sumOf { it.runDuration }
                    val totalMinutes = totalSeconds / 60
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60

                    val totalDays = runList.size

                    binding.tvTotalDistance.text = "$totalDistance km"
                    binding.tvTotalDuration.text = "${hours} jam ${minutes} menit"
                    binding.tvTotalDays.text = "$totalDays hari"
                }
        }
    }

    override fun onResume() {
        super.onResume()

        val db = AppDatabase.getDatabase(requireContext())
        val userDao = db.userDao()

        // 🔥 Ambil data user (tetap pakai coroutine, ini aman)
        lifecycleScope.launch {
            val user = userDao.getLastUser()

            if (user != null) {
                binding.tvName.text = user.nama
                binding.tvEmail.text = user.username
            }
        }
    }
}