package ru.kabirov.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kabirov.database.dao.IpSearcherDao
import ru.kabirov.database.models.CheckAllSubnetForOrganisation
import ru.kabirov.database.models.SubnetDbo
import ru.kabirov.database.models.OrganisationDbo
import ru.kabirov.database.models.OrganisationNameQuery

class IpSearcherDatabase internal constructor(private val database: IpSearcherRoomDatabase) {
    val ipSearcherDao: IpSearcherDao
        get() = database.ipSearcherDao()
}

@Database(
    entities = [
        SubnetDbo::class,
        OrganisationDbo::class,
        OrganisationNameQuery::class,
        CheckAllSubnetForOrganisation::class,
    ],
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