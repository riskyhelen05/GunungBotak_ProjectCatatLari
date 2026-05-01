package com.upn.catatlari.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn.catatlari.databinding.ItemRunBinding
import com.upn.catatlari.data.local.entity.RunEntity

class RunAdapter : RecyclerView.Adapter<RunAdapter.ViewHolder>() {

    private var runList = emptyList<RunEntity>()

    class ViewHolder(val binding: ItemRunBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun formatDistance(meter: Int): String {
        return if (meter >= 1000) {
            "${meter / 1000.0} km"
        } else {
            "$meter m"
        }
    }

    private fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

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

        holder.binding.txtRunDate.text = currentItem.runDate
        holder.binding.txtRunDistance.text = formatDistance(currentItem.runDistance)
        holder.binding.txtRunDuration.text = formatDuration(currentItem.runDuration)
    }

    fun setData(runItems: List<RunEntity>) {
        this.runList = runItems
        notifyDataSetChanged()
    }
}