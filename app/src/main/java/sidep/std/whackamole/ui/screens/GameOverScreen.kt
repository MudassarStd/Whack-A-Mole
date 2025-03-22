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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import sidep.std.whackamole.data.local.LeaderBoardScore
import sidep.std.whackamole.game.GameState
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.navigation.Routes


@Composable
fun GameOverScreen(
    navController: NavHostController,
    viewModel: GameViewModel,
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        val scoreItem = LeaderBoardScore(
//            score = gameState.score,
//            playerName = gameState.player
//        )
//        viewModel.add(scoreItem)
//    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(elevation = CardDefaults.cardElevation(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Your Score: ${gameState.score}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    TextButton(onClick = {
                        navController.navigate(Routes.StartGameScreen.route) {
                            popUpTo(Routes.StartGameScreen.route) { inclusive = true }
                        }
                    }) { Text("Play Again") }

                    Spacer(Modifier.width(12.dp))

                    TextButton(onClick = {
                        navController.navigate(Routes.LeaderBoardScreen.route)
//                        viewModel.resetGameConfig()
                    }) { Text("Leader board") }

                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun LeaderBoardScreen(
    viewModel: GameViewModel = koinViewModel()
) {

    val scores by viewModel.scores.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.padding(horizontal = 16.dp), elevation = CardDefaults.cardElevation(12.dp)) {
            Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                Row (horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Leader board", fontWeight = FontWeight.Bold)

                    if (scores.isNotEmpty()) {
                        TextButton(onClick = { viewModel.deleteAll() }) {
                            Text("Delete All")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                LazyColumn {
                    items(scores) { score ->
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Player: ${score.playerName}")
                            Text("Score: ${score.score}")
                        }
                    }
                }

                if (scores.isEmpty()) {
                    Text("No scores found")
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun GameOverPreview() {
    GameOverScreen(navController = rememberNavController(), viewModel = koinViewModel<GameViewModel>())
}