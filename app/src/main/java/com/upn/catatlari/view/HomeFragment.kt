package com.upn.catatlari.view

import android.app.AlertDialog // Pastikan import ini ada
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.upn.catatlari.databinding.FragmentHomeBinding
import com.upn.catatlari.viewmodel.RunViewModel
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val runViewModel: RunViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // AMBIL DATA NAMA DAN ID DARI SESSION
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", -1)
        val userName = sharedPref.getString("userName", "Pelari")

        if (userId == -1) {
            Log.e("HOME", "User belum login tapi bisa masuk Home!")
            return
        }

        // TAMPILKAN NAMA
        binding.welcomingTxt.text = "Halo, $userName"

        val runAdapter = RunAdapter()
        binding.rvRunList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = runAdapter
        }

        runViewModel.getRunsByUser(userId).observe(viewLifecycleOwner) { runList ->
            Log.d("HOME_RUN", "Data diterima: $runList")
            runAdapter.setData(runList)

            // LOGIKA EMPTY STATE
            if (runList.isEmpty()) {
                binding.rvRunList.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvRunList.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
                binding.rvRunList.scrollToPosition(runList.size - 1)
            }
        }

        // TOMBOL TAMBAH
        binding.floatingBtnAddRun.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddRunFragment()
            )
        }

        binding.btnLogout.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Keluar Akun")
                .setMessage("Apakah kamu yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya, Keluar") { dialog, _ ->
                    // Kalau pilih Ya, eksekusi logout
                    with(sharedPref.edit()) {
                        clear()
                        apply()
                    }

                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()

                    Toast.makeText(requireContext(), "Berhasil Logout!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}