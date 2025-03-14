package sidep.std.whackamole.ui.screens

import android.text.Layout.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import sidep.std.whackamole.R
import sidep.std.whackamole.game.GameState
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.navigation.Routes

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GameViewModel = viewModel()
) {
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(painter = painterResource(R.drawable.mole), contentDescription = null)

        Text("Whack A Mole", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        Text("Difficulty Level")

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = false, onClick = { } , label = { Text("Nobe") })
            FilterChip(selected = false, onClick = { } , label = { Text("Pro") })
            FilterChip(selected = false, onClick = { } , label = { Text("Master") })
        }

        Text("Plain size")

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp) ){
            FilterChip(selected = false, onClick = { } , label = { Text("3x3") })
            FilterChip(selected = false, onClick = { } , label = { Text("6x6") })
            FilterChip(selected = false, onClick = { } , label = { Text("9x9") })
        }

        Button(onClick = {
            viewModel.updateGameActive(true)
            navController.navigate(Routes.GameScreen.route)
        }) { Text("Start Game") }
    }
}


@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    StartScreen(navController = rememberNavController())
}