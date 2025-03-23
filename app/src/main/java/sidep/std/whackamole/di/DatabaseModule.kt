package sidep.std.whackamole.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import sidep.std.whackamole.data.local.ScoreDb

val databaseModule = module {

    // providing a single room db instance
    single {
        Room.databaseBuilder(
            androidContext(),
            ScoreDb::class.java,
            "leader_board_db"
        ).fallbackToDestructiveMigration().build()
    }

    // provide singleton dao instance
    single { get<ScoreDb>().leaderBoardDao() }
    single { get<ScoreDb>().playerDao() }
}