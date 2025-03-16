package sidep.std.whackamole.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    var gameConfig: GameConfig = GameConfig()

    private var gameJob: Job? = null

    fun startGame() {
        _gameState.value = GameState(isActive = true, score = 0, elapsedTime = 0, molePosition = -1)

        gameJob?.cancel()

        gameJob = viewModelScope.launch {
            launch { startTimer() }
            launch { controlMoleAppearance() }
        }
    }

    private suspend fun startTimer() {
        while (_gameState.value.isActive) {
            delay(1000)
            _gameState.value = _gameState.value.copy(elapsedTime = _gameState.value.elapsedTime + 1)
        }
    }

    private suspend fun controlMoleAppearance() {
        val gridSize = gameConfig.plain.size * gameConfig.plain.size
        val moleSpeed = gameConfig.difficultyLevel.gameSpeed

        while (_gameState.value.isActive) {
            _gameState.value = _gameState.value.copy(molePosition = Random.nextInt(gridSize))
            delay(moleSpeed)
            // hide mole
            _gameState.value = _gameState.value.copy(molePosition = -1)
            delay(moleSpeed)
        }
    }

    fun whackMole() {
        if (_gameState.value.molePosition != -1) {
            _gameState.value = _gameState.value.copy(
                score = _gameState.value.score + 1,
                molePosition = -1  // Hide mole after being hit
            )
        }
    }

    fun stopGame() {
        _gameState.value = _gameState.value.copy(isActive = false)
        gameJob?.cancel()
    }

    fun resetGameConfig() {
        _gameState.value = GameState()
        gameConfig = GameConfig()
    }
}

