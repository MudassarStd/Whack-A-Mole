package sidep.std.whackamole.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sidep.std.whackamole.data.repository.LeaderBoardRepository
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.screens.ScoreViewModel

val appModule = module {
    single { LeaderBoardRepository(get()) }

//    viewModel { ScoreViewModel(get()) }
    viewModel { GameViewModel(get()) }
}