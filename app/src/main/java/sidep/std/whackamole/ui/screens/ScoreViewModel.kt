package sidep.std.whackamole.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sidep.std.whackamole.data.local.LeaderBoardScore
import sidep.std.whackamole.data.repository.LeaderBoardRepository

class ScoreViewModel(
    private val leaderBoardRepository: LeaderBoardRepository
): ViewModel() {

    // path 1: to get flow from room and update UI
    // convert flow into state flow
    val scores: StateFlow<List<LeaderBoardScore>> = leaderBoardRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun add(leaderBoardScore: LeaderBoardScore) {
        Log.d("TestingAddOp", "add called")
        viewModelScope.launch {
            leaderBoardRepository.add(leaderBoardScore)
        }
    }

    fun deleteAll() = viewModelScope.launch {
        leaderBoardRepository.deleteAll()
    }
}