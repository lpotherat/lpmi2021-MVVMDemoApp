package lpmi.potherat.promo2021.mvvmdemoapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.security.cert.CertificateParsingException;
import java.util.List;

import lpmi.potherat.promo2021.mvvmdemoapp.model.Resultat;

/**
 * Data Access Object pour les résultats d'opérations.
 *
 * On déclare ici une interface avec l'annotation @Dao pour que Room génère les méthodes
 * correspondantes aux requêtes déclarées en annotation.
 *
 */
@Dao
public interface ResultatDao {

    /**
     * @return la liste de tous les résultats
     */
    //On déclare ici une annotation @Query avec la requête SQL correspondante et le type de données
    //attendu, Room génèrera l'implémentation correspondante.
    @Query("SELECT * FROM resultat")
    List<Resultat> getAll();

    //Pour les requêtes Insert, Delete ou Update, il suffit d'ajouter l'annotation correspondante
    //sans la requête.

    //La requête Insert peut être complétée avec un paramètre de choix de stratégie pour l'insertion
    //si la clé primaire passée est déjà présente en base. On choisit ici REPLACE.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Resultat resultat);

    @Delete
    void delete(Resultat resultat);

}
