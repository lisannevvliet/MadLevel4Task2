package com.example.madlevel4task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.databinding.ItemHistoryBinding

class HistoryAdapter(private val history: List<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHistoryBinding.bind(itemView)

        // Specify which data in History.kt corresponds to which TextViews and ImageViews.
        fun databind(history: History) {
            binding.tvResult.text = history.result
            binding.tvTimestamp.text = history.timestamp.toString()

            when (history.computerMove) {
                1 -> binding.ivComputer.setImageResource(R.drawable.rock)
                2 -> binding.ivComputer.setImageResource(R.drawable.paper)
                3 -> binding.ivComputer.setImageResource(R.drawable.scissors)
            }

            when (history.playerMove) {
                1 -> binding.ivYou.setImageResource(R.drawable.rock)
                2 -> binding.ivYou.setImageResource(R.drawable.paper)
                3 -> binding.ivYou.setImageResource(R.drawable.scissors)
            }
        }
    }

    // Create and return a ViewHolder object, inflate a standard layout called simple_list_item_1.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    // Return the size of the list.
    override fun getItemCount(): Int {
        return history.size
    }

    // Display the data at the specified position, called by RecyclerView.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(history[position])
    }
}