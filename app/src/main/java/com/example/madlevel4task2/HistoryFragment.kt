package com.example.madlevel4task2

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.databinding.FragmentHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// A simple [Fragment] subclass as the second destination in the navigation.
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val history = arrayListOf<History>()
    private val historyAdapter = HistoryAdapter(history)
    private lateinit var historyRepository: HistoryRepository

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the HistoryRepository.
        historyRepository = HistoryRepository(requireContext())

        // Enable a different AppBar for this fragment.
        setHasOptionsMenu(true)

        // Display the different AppBar and show a button to navigate back to the GameFragment.
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Your Game History"
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_vector_test)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getHistory()
        initRv()
    }

    // Inflate the custom AppBar.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Delete all database entries upon a click on the AppBar trash can.
            R.id.action_delete -> {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) { historyRepository.deleteAll() }
                    history.clear()
                    historyAdapter.notifyDataSetChanged()
                }
                true
            }
            // Navigate to the GameFragment upon a click on the AppBar back arrow and clear the back stack.
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Retrieve the history from the database and update the RecyclerView.
    private fun getHistory() {
        CoroutineScope(Dispatchers.Main).launch {
            val allHistory = withContext(Dispatchers.IO) { historyRepository.getAll() }
            history.clear()
            history.addAll(allHistory)
            historyAdapter.notifyDataSetChanged()
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter.
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = historyAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}