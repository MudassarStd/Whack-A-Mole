package sidep.std.whackamole.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [LeaderBoardScore::class], version = 1, exportSchema = false)
abstract class ScoreDb: RoomDatabase() {
    abstract fun leaderBoardDao(): LeaderBoardDao
}

@Dao
interface LeaderBoardDao {
    @Insert
    suspend fun add(leaderBoardScore: LeaderBoardScore)

    @Query("select * from LeaderBoardScore order by score desc ")
    fun getAll(): Flow<List<LeaderBoardScore>>

    @Query("delete from LeaderBoardScore")
    suspend fun deleteAll()
}

@Entity
data class LeaderBoardScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val playerName: String
)
