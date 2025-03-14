package sidep.std.whackamole.ui.navigation

sealed class Routes(val route: String) {
    data object StartGameScreen: Routes("start")
    data object GameOverScreen: Routes("over")
    data object GameScreen: Routes("mainGame")
}