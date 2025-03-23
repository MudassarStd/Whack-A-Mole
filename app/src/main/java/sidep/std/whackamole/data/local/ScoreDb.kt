package sidep.std.whackamole.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [LeaderBoardScore::class, Player::class], version = 1, exportSchema = false)
abstract class ScoreDb: RoomDatabase() {
    abstract fun leaderBoardDao(): LeaderBoardDao
    abstract fun playerDao(): PlayerDao
}

@Dao
interface LeaderBoardDao {
    @Insert
    suspend fun add(leaderBoardScore: LeaderBoardScore)

    @Query("select * from LeaderBoardScore order by score desc, gameplayTime asc ")
    fun getAll(): Flow<List<LeaderBoardScore>>

    @Query("delete from LeaderBoardScore")
    suspend fun deleteAll()
}

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(player: Player)

    @Query("select * from Player ")
    fun getAll(): Flow<List<Player>>

    @Query("delete from Player")
    suspend fun deleteAll()
}

@Entity(
    foreignKeys = [ ForeignKey(
        entity = Player::class,
        parentColumns = ["playerName"],
        childColumns = ["playerName"],
        onDelete = ForeignKey.CASCADE
    ) ]
)
data class LeaderBoardScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val playerName: String,
    val gameplayTime: Int
)

@Entity
data class Player(
    @PrimaryKey
    val playerName: String
)

// get players with most scores (summed up scores)