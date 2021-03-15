package com.example.madlevel4task2

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.madlevel4task2.databinding.FragmentGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

// A simple [Fragment] subclass as the default destination in the navigation.
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyRepository: HistoryRepository

    private var playerMove: Int = 0
    private var computerMove: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the HistoryRepository.
        historyRepository = HistoryRepository(requireContext())

        // Enable a different AppBar for this fragment.
        setHasOptionsMenu(true)

        // Ensure that the AppBar will change back after coming from the HistoryFragment.
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.app_name)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        // Only show the statistics if the database is not empty.
        CoroutineScope(Dispatchers.Main).launch {
            var database = withContext(Dispatchers.IO) { historyRepository.getAll() }
            if (!database.isNullOrEmpty()) { updateUI() }
        }

        binding.ivRock.setOnClickListener {
            playerMove = 1
            checkResult()
        }

        binding.ivPaper.setOnClickListener {
            playerMove = 2
            checkResult()
        }

        binding.ivScissors.setOnClickListener {
            playerMove = 3
            checkResult()
        }
    }

    // Inflate the custom AppBar.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.game_menu, menu)
    }

    // Navigate to the HistoryFragment upon a click on the AppBar history icon.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                findNavController().navigate(R.id.action_gameFragment_to_historyFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Generate a random computer move, add the results to the database and show the winner.
    private fun checkResult() {
        computerMove = (1..3).random()

        if (computerMove == playerMove) {
            CoroutineScope(Dispatchers.Main).launch {
                val history = History(timestamp = Date(), computerMove = computerMove, playerMove = playerMove, result = getString(R.string.draw))
                withContext(Dispatchers.IO) { historyRepository.insert(history) }
                updateUI()
            }
            binding.tvResult.text = getString(R.string.draw)

        } else if (computerMove == 1 && playerMove == 2 || computerMove == 2 && playerMove == 3 || computerMove == 3 && playerMove == 1) {
            CoroutineScope(Dispatchers.Main).launch {
                val history = History(timestamp = Date(), computerMove = computerMove, playerMove = playerMove, result = getString(R.string.you_win))
                withContext(Dispatchers.IO) { historyRepository.insert(history) }
                updateUI()
            }
            binding.tvResult.text = getString(R.string.you_win)

        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val history = History(timestamp = Date(), computerMove = computerMove, playerMove = playerMove, result = getString(R.string.computer_wins))
                withContext(Dispatchers.IO) { historyRepository.insert(history) }
                updateUI()
            }
            binding.tvResult.text = getString(R.string.computer_wins) }
    }

    // Update the user interface with the correct statistics and images.
    private fun updateUI() {

        CoroutineScope(Dispatchers.Main).launch {
            var win = withContext(Dispatchers.IO) { historyRepository.getWin() }
            var draw = withContext(Dispatchers.IO) { historyRepository.getDraw() }
            var lose = withContext(Dispatchers.IO) { historyRepository.getLose() }

            binding.tvStatistics.text = getString(R.string.statistics)
            binding.tvWinDrawLose.text = getString(R.string.win_draw_lose, win, draw, lose)
        }

        when (computerMove) {
            1 -> binding.ivComputer.setImageResource(R.drawable.rock)
            2 -> binding.ivComputer.setImageResource(R.drawable.paper)
            3 -> binding.ivComputer.setImageResource(R.drawable.scissors)
        }

        when (playerMove) {
            1 -> binding.ivYou.setImageResource(R.drawable.rock)
            2 -> binding.ivYou.setImageResource(R.drawable.paper)
            3 -> binding.ivYou.setImageResource(R.drawable.scissors)
        }
    }
}