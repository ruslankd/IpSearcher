package ru.kabirov.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kabirov.database.dao.IpSearcherDao
import ru.kabirov.database.models.Network
import ru.kabirov.database.models.NetworkRange
import ru.kabirov.database.models.Organisation

class IpSearcherDatabase internal constructor(private val database: IpSearcherRoomDatabase) {
    val ipSearcherDao: IpSearcherDao
        get() = database.ipSearcherDao()
}

@Database(
    entities = [Network::class, NetworkRange::class, Organisation::class],
    version = 1,
    exportSchema = true,
)
internal abstract class IpSearcherRoomDatabase : RoomDatabase() {
    abstract fun ipSearcherDao(): IpSearcherDao
}

fun IpSearcherDatabase(applicationContext: Context): IpSearcherDatabase {
    return IpSearcherDatabase(
        Room.databaseBuilder(
            applicationContext,
            IpSearcherRoomDatabase::class.java,
            "ipSearcher"
        ).build()
    )
}