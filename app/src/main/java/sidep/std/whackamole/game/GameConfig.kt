package sidep.std.whackamole.game

data class GameConfig (
    val difficultyLevel: Difficulty = Difficulty.NOOB,
    val plain: Plain = Plain.SMALL
)

enum class Difficulty(val title: String, val gameSpeed: Long) {
    NOOB("Noob",2000), PRO("Pro", 1500), MASTER("Master",1200)
}

enum class Plain(val size: Int, val title: String) {
    SMALL(3, "Small"), MEDIUM(6, "Medium"), LARGE(9, "Large")
}