package sidep.std.whackamole.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.navigation.Routes

@Composable
fun GameOverScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: GameViewModel = viewModel()
) {
    val score = viewModel.gameState.value.score
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Your Score is: $score")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {
                        // pop this screen from nav stack
                        navController.navigate(Routes.StartGameScreen.route)
                    }) { Text("Main Menu") }

                    Spacer(Modifier.width(8.dp))

                    Button(onClick = {
                        // pop this screen from nav stack
                        navController.navigate(Routes.GameScreen.route)
                    }) { Text("Play Again") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameOverPreview() {
    GameOverScreen(navController = rememberNavController())
}