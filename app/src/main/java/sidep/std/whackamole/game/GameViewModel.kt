package sidep.std.whackamole.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var _gameState = mutableStateOf(GameState())
    val gameState : State<GameState> get() = _gameState

    fun updateGameActive(isActive: Boolean) {
        _gameState.value = _gameState.value.copy(isActive = isActive)
    }

    fun updateScore(score: Int) {
        _gameState.value = gameState.value.copy(score = score)
    }
}
