package lpmi.potherat.promo2021.mvvmdemoapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Représente un résultat d'opération dans la base de données
 *
 * On utilise l'annotation @Entity pour permettre à room de générer automatiquement la table
 * correspondante dans la base de données
 */
@Entity
public class Resultat {

    /**
     * On utilise ici l'annotation @PrimaryKey pour déclarer la clé primaire,
     * puis on passe le paramètre autoGenerate à true pour activer l'autoincrement.
     */
    @PrimaryKey(autoGenerate = true)
    long id;

    Integer valeur1;
    Integer valeur2;
    Integer resultat;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getValeur1() {
        return valeur1;
    }

    public void setValeur1(Integer valeur1) {
        this.valeur1 = valeur1;
    }

    public Integer getValeur2() {
        return valeur2;
    }

    public void setValeur2(Integer valeur2) {
        this.valeur2 = valeur2;
    }

    public Integer getResultat() {
        return resultat;
    }

    public void setResultat(Integer resultat) {
        this.resultat = resultat;
    }
}
