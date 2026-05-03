package com.upn.catatlari.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.upn.catatlari.R
import com.upn.catatlari.databinding.FragmentHomeBinding
import com.upn.catatlari.viewmodel.RunViewModel
import com.upn.catatlari.data.local.entity.RunEntity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.upn.catatlari.data.local.database.AppDatabase
import kotlinx.coroutines.launch
import java.util.Calendar

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

    override fun onResume() {
        super.onResume()

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", -1)

        if (userId != -1) {
            loadUserData(userId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", -1)

        if (userId == -1) {
            Log.e("HOME", "User ticak valid.")
            return
        }

        loadUserData(userId)

        val runAdapter = RunAdapter()

        runAdapter.onDeleteClick = { run ->
            AlertDialog.Builder(requireContext())
                .setTitle("Hapus Catatan")
                .setMessage("Apakah kamu yakin ingin menghapus catatan lari tanggal ${run.runDate}?")
                .setPositiveButton("Ya, Hapus") { dialog, _ ->
                    runViewModel.deleteRun(run)
                    Toast.makeText(requireContext(), "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        runAdapter.onEditClick = { run ->
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_run, null)
            val etDate = dialogView.findViewById<EditText>(R.id.et_edit_date)
            val etDistance = dialogView.findViewById<EditText>(R.id.et_edit_distance)
            val etDuration = dialogView.findViewById<EditText>(R.id.et_edit_duration)

            etDate.setText(run.runDate)
            etDistance.setText(run.runDistance.toString())
            etDuration.setText((run.runDuration / 60).toString())

            etDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    etDate.setText("$formattedDay-$formattedMonth-$selectedYear")
                }, year, month, day).show()
            }

            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Simpan Perubahan") { dialog, _ ->
                    val newDate = etDate.text.toString()
                    val newDistanceStr = etDistance.text.toString()
                    val newDurationStr = etDuration.text.toString()

                    if (newDate.isEmpty() || newDistanceStr.isEmpty() || newDurationStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val newDistance = newDistanceStr.toIntOrNull()
                    val newDurationInMinutes = newDurationStr.toIntOrNull()

                    if (newDistance == null || newDurationInMinutes == null || newDistance <= 0 || newDurationInMinutes <= 0) {
                        Toast.makeText(requireContext(), "Input tidak valid!", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val updatedRun = RunEntity(
                        id = run.id,
                        runDate = newDate,
                        runDistance = newDistance,
                        runDuration = newDurationInMinutes * 60,
                        userId = run.userId
                    )

                    runViewModel.updateRun(updatedRun)
                    Toast.makeText(requireContext(), "Data berhasil diubah!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.rvRunList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = runAdapter
        }

        runViewModel.getRunsByUser(userId).observe(viewLifecycleOwner) { runList ->
            Log.d("HOME_RUN", "Data diterima: $runList")
            runAdapter.setData(runList)

            if (runList.isEmpty()) {
                binding.rvRunList.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvRunList.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
                binding.rvRunList.scrollToPosition(runList.size - 1)
            }
        }

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
    private fun loadUserData(userId: Int) {
        val db = AppDatabase.getDatabase(requireContext())
        val userDao = db.userDao()

        lifecycleScope.launch {
            val user = userDao.getUserById(userId)

            if (user != null) {
                binding.welcomingTxt.text = "Halo, ${user.nama}"
            } else {
                binding.welcomingTxt.text = "Halo, Pelari"
            }
        }
    }
}