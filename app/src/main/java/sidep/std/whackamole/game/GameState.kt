package sidep.std.whackamole.game

data class GameState(
    val isActive: Boolean = true,
    val score: Int = 0,
    val gridSize: Int = 3,
    val gameSpeed: GameSpeed = GameSpeed.Nobe
)


enum class GameSpeed {
    Nobe, Pro, GrandMaster
}