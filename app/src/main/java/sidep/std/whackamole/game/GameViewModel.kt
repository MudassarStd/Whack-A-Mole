package sidep.std.whackamole.game

import android.app.Application
import android.content.Context
import android.media.SoundPool
import android.util.Log
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sidep.std.whackamole.R
import sidep.std.whackamole.data.local.LeaderBoardScore
import sidep.std.whackamole.data.local.Player
import sidep.std.whackamole.data.repository.LeaderBoardRepository
import kotlin.random.Random

class GameViewModel(
    private val leaderBoardRepository: LeaderBoardRepository
) : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    var gameConfig: GameConfig = GameConfig()

    private var gameJob: Job? = null

    fun initSound(context: Context) {
        SoundPoolObj.init(context)
    }

    fun initGame(player: String) {
        _gameState.value = GameState(isActive = true, player = player) // creates a new state of game with isActive = true
        // adding player to list
        addPlayer(playerName = player)
    }


    // these just update existing state
    fun resumeGame(isActive: Boolean = true) {
        _gameState.value = _gameState.value.copy(isActive = isActive)
    }

    fun runGame() {
//        _gameState.value = GameState(isActive = true, score = 0, elapsedTime = 0, molePosition = -1)

        // checks and cancels any previous game job
        gameJob?.cancel()

        gameJob = viewModelScope.launch {
            // structured concurrency, launching 2 coroutines inside a parent scope
            launch {
                gameState.collectLatest { state ->
                    while (state.isActive) {
                        delay(1000)
                        _gameState.value =
                            _gameState.value.copy(elapsedTime = _gameState.value.elapsedTime + 1)
                    }
                }
            }

            launch {
                val gridSize = gameConfig.plain.size * gameConfig.plain.size
                val moleSpeed = gameConfig.difficultyLevel.gameSpeed

                gameState.collectLatest { state ->
                    while (state.isActive) {
                        _gameState.value = _gameState.value.copy(molePosition = Random.nextInt(gridSize))
                        delay(moleSpeed) // position delay
                        // hide mole
                        _gameState.value = _gameState.value.copy(molePosition = -1)
                        delay(moleSpeed) // hide delay
                    }
                }
            }
        }
    }

    fun whackMole() {
        SoundPoolObj.playSound()
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

    override fun onCleared() {
        super.onCleared()
        SoundPoolObj.clean()
    }


    /**
     * Leader board operations
     **/

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

    /**
     * Player operations
     **/

    private fun addPlayer(playerName: String) {
        viewModelScope.launch {
            leaderBoardRepository.addPlayer(Player(playerName = playerName))
        }
    }

    val players: StateFlow<List<Player>> = leaderBoardRepository.getAllPlayers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deletePlayers() = viewModelScope.launch { leaderBoardRepository.deleteAllPlayers() }

}

object SoundPoolObj {

    private var soundPool: SoundPool? = null
    private var tapSoundId: Int = 0
    private var isInitialized: Boolean = false

    fun init(context: Context) {
        if (isInitialized) return

        soundPool = SoundPool.Builder().setMaxStreams(3).build()
        tapSoundId = soundPool?.load(context, R.raw.screen_tap, 1) ?: 0
        isInitialized = true
    }

    fun playSound() {
        soundPool?.play(tapSoundId, 1f,1f, 0,0,1f)
    }

    fun clean() {
        soundPool?.release()
        soundPool = null
    }
}