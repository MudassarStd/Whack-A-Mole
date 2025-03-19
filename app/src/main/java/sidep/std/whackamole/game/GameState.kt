package sidep.std.whackamole.game

data class GameState(
    val isActive: Boolean = false,
    val score: Int = 0,
    val elapsedTime: Int = 0,
    val molePosition: Int = -1
)