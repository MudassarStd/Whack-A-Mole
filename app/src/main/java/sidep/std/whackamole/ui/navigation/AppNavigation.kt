package sidep.std.whackamole.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.screens.GameOverScreen
import sidep.std.whackamole.ui.screens.GameScreen
import sidep.std.whackamole.ui.screens.StartScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier, viewModel: GameViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.StartGameScreen.route) {
        composable(Routes.StartGameScreen.route) { StartScreen(navController = navController, viewModel = viewModel) }
        composable(Routes.GameOverScreen.route) { GameOverScreen(navController = navController, viewModel = viewModel) }
        composable(Routes.GameScreen.route) { GameScreen(modifier = Modifier.fillMaxSize().systemBarsPadding(), navController = navController, viewModel = viewModel) }
    }
}

