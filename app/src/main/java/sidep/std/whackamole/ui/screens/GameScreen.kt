package sidep.std.whackamole.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import sidep.std.whackamole.R
import sidep.std.whackamole.game.GameConfig
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.navigation.Routes
import kotlin.random.Random


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()

    LaunchedEffect(gameState.isActive) {
        if (gameState.isActive) {
            viewModel.startGame()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val minutes = gameState.elapsedTime / 60
            val seconds = gameState.elapsedTime % 60
            val formattedTime = String.format("%02d:%02d", minutes, seconds)

            Text("Time: $formattedTime")
            Text("Score: ${gameState.score}", fontWeight = FontWeight.Bold)
        }

        LazyVerticalGrid(columns = GridCells.Fixed(viewModel.gameConfig.plain.size)) {
            items(viewModel.gameConfig.plain.size * viewModel.gameConfig.plain.size) { index ->
                if (gameState.molePosition == index) {
                    Image(
                        painter = painterResource(R.drawable.mole),
                        contentDescription = null,
                        modifier = Modifier.clickable { viewModel.whackMole() }
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.stopGame()
                navController.navigate(Routes.GameOverScreen.route)
            }
        ) {
            Text("End Game")
        }
    }
}

@Composable
fun RandomizeMolePosition(time: Long, gridSize: Int, pos: (Int) -> Unit) {
    LaunchedEffect(true) {
        while (true) {
            delay(time)
            pos(Random.nextInt(9))
        }
    }
}

@Composable
fun Timer(isActive: Boolean, onTick: () -> Unit) {
    // fires a coroutine tied to this comp's lifecycle
    // runs only once, when comp starts
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1000)
            onTick()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGameScreen() {
    GameScreen(Modifier.fillMaxSize(), navController = rememberNavController())
}