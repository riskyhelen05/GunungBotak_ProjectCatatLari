package com.upn.catatlari.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn.catatlari.databinding.ItemRunBinding
import com.upn.catatlari.model.Run

class RunAdapter : RecyclerView.Adapter<RunAdapter.ViewHolder>() {

    private var runList = emptyList<Run>()

    class ViewHolder(val binding: ItemRunBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRunBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return runList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = runList[position]

        // ⚠️ sesuaikan dengan id di item_run.xml kamu
        holder.binding.txtRunDate.text = currentItem.runDate
        holder.binding.txtRunDistance.text = currentItem.runDistance.toString()
        holder.binding.txtRunDuration.text = currentItem.runDuration.toString()
    }

    // ✅ INI PENTING BANGET
    fun setData(runItems: List<Run>) {
        this.runList = runItems
        notifyDataSetChanged() // 🔥 WAJIB ADA
    }
}