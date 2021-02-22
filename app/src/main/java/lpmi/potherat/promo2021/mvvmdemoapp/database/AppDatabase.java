package lpmi.potherat.promo2021.mvvmdemoapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import lpmi.potherat.promo2021.mvvmdemoapp.dao.ResultatDao;
import lpmi.potherat.promo2021.mvvmdemoapp.model.Resultat;

/**
 * Déclaration de la base de données.
 *
 * C'est une classe abstraite qui hérite de RoomDatabase, accompagnée d'une annotation @Database.
 *
 * Cette annotation doit contenir la liste des entités qui vont composer la base de données
 * ainsi que la version de la base de données.
 *
 * La version permet à room de gérer la migration de base de données lorsque la structure est
 * amenée à changer.
 *
 * On y déclare ensuite l'ensemble des méthodes qui permettront à Room de fournir les Dao
 * nécessaires.
 */
@Database(entities = {Resultat.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ResultatDao getResultatDao();

}
