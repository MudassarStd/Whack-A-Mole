package sidep.std.whackamole.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    viewModel: GameViewModel
) {
    val gameState by viewModel.gameState.collectAsState()
    val gridSize = viewModel.gameConfig.plain.size * viewModel.gameConfig.plain.size

    val context = LocalContext.current

    // init context
    LaunchedEffect(Unit) {
        viewModel.initSound(context)
    }

    LaunchedEffect(gameState.isActive) // for now, this key is un-effective, should be some kind of game pause/resume key
    {
        if (gameState.isActive) {
            viewModel.runGame()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // timer + score row
        Row(modifier = Modifier.fillMaxWidth().padding(all = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            val minutes = gameState.elapsedTime / 60
            val seconds = gameState.elapsedTime % 60
            val formattedTime = String.format("%02d:%02d", minutes, seconds)

            Text("Time: $formattedTime", fontSize = 18.sp)
            Text("Score: ${gameState.score}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(viewModel.gameConfig.plain.size),
            modifier = Modifier
                .weight(1f)
                .background(Color.Green)
                .clickable {
                    viewModel.stopGame()
                    navController.navigate(Routes.GameOverScreen.route)
                },
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gridSize) { index ->
                if (gameState.molePosition == index) {
                Image(
                    painter = painterResource(R.drawable.mole),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { viewModel.whackMole() }
                        .size(80.dp)
                )
                }
            }
        }


        Row(modifier = Modifier.padding(16.dp)) {

            Button(onClick = { if (gameState.isActive) viewModel.resumeGame(false) else viewModel.resumeGame() }) {
                Text(if (gameState.isActive) "Pause" else "Resume")
            }
            Spacer(Modifier.width(12.dp))

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
}

@Preview(showBackground = true)
@Composable
private fun PreviewGameScreen() {
    GameScreen(Modifier.fillMaxSize(), navController = rememberNavController(), GameViewModel())
}