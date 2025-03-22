package sidep.std.whackamole.data.repository

import kotlinx.coroutines.flow.Flow
import sidep.std.whackamole.data.local.LeaderBoardDao
import sidep.std.whackamole.data.local.LeaderBoardScore

class LeaderBoardRepository(
    private val leaderBoardDao: LeaderBoardDao
) {
    suspend fun add(leaderBoardScore: LeaderBoardScore) {
        leaderBoardDao.add(leaderBoardScore)
    }

    fun getAll(): Flow<List<LeaderBoardScore>> = leaderBoardDao.getAll()

    suspend fun deleteAll() = leaderBoardDao.deleteAll()
}
