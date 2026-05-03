package com.upn.catatlari.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.upn.catatlari.databinding.FragmentAddRunBinding
import com.upn.catatlari.viewmodel.RunViewModel
import com.upn.catatlari.data.local.entity.RunEntity
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar

class AddRunFragment : Fragment() {

    private lateinit var binding: FragmentAddRunBinding
    private val runViewModel: RunViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val dateString = "$formattedDay-$formattedMonth-$selectedYear"
                    binding.etDate.setText(dateString)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        binding.btnSaveRun.setOnClickListener {

            val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("userId", -1)

            if (userId == -1) {
                Toast.makeText(requireContext(), "User tidak ditemukan, silakan login ulang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val runDate = binding.etDate.text.toString()
            val runDistance = binding.etRunDistance.text.toString()
            val runDuration = binding.etRunDuration.text.toString()

            if (runDate.isEmpty() || runDistance.isEmpty() || runDuration.isEmpty()) {
                Toast.makeText(requireContext(), "Kolom tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val distance = runDistance.toIntOrNull()
            val durationInMinutes = runDuration.toIntOrNull()

            if (distance == null || durationInMinutes == null) {
                Toast.makeText(requireContext(), "Jarak & durasi harus berupa angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (distance <= 0 || durationInMinutes <= 0) {
                Toast.makeText(requireContext(), "Jarak & durasi tidak boleh 0!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val durationInSeconds = durationInMinutes * 60

            val runEntity = RunEntity(
                runDate = runDate,
                runDistance = distance,
                runDuration = durationInSeconds,
                userId = userId
            )

            runViewModel.addRun(runEntity)
            Toast.makeText(requireContext(), "Data lari berhasil disimpan!", Toast.LENGTH_SHORT).show()

            view.post {
                findNavController().popBackStack()
            }
        }
    }
}