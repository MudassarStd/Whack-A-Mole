package sidep.std.whackamole.data.repository

import kotlinx.coroutines.flow.Flow
import sidep.std.whackamole.data.local.LeaderBoardDao
import sidep.std.whackamole.data.local.LeaderBoardScore
import sidep.std.whackamole.data.local.Player
import sidep.std.whackamole.data.local.PlayerDao

class LeaderBoardRepository(
    private val leaderBoardDao: LeaderBoardDao,
    private val playerDao: PlayerDao
) {
    suspend fun add(leaderBoardScore: LeaderBoardScore) {
        leaderBoardDao.add(leaderBoardScore)
    }

    fun getAll(): Flow<List<LeaderBoardScore>> = leaderBoardDao.getAll()

    suspend fun deleteAll() = leaderBoardDao.deleteAll()


    suspend fun addPlayer(player: Player) = playerDao.add(player)

    fun getAllPlayers(): Flow<List<Player>> = playerDao.getAll()
    suspend fun deleteAllPlayers() = playerDao.deleteAll()

}
