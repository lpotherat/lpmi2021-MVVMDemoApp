package lpmi.potherat.promo2021.mvvmdemoapp;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.room.Room;

import java.util.concurrent.Executors;

import lpmi.potherat.promo2021.mvvmdemoapp.dao.ResultatDao;
import lpmi.potherat.promo2021.mvvmdemoapp.database.AppDatabase;
import lpmi.potherat.promo2021.mvvmdemoapp.model.Resultat;

/**
 * ViewModel attaché à MainActivity.
 * Le viewmodel est en charge de faire le lien entre la couche de model et la vue.
 * Ce viewmodel hérite de AndroidViewModel, cela permet d'accéder à l'Application au sein du
 * viewmodel et ainsi d'accéder à notre base de données locale.
 *
 * Ce viewmodel expose 3 paramètres, deux en écriture, et un en lecture seule.
 *
 * Les paramètres en lecture écriture sont les deux valeurs qui serviront à l'addition,
 * le paramètre en lecture seule est le résultat de l'addition.
 *
 * De cette façon, la séparation entre la logique et la présentation est bien réalisée, et permet :
 * - une meilleure maîtrise du code en général
 * - l'automatisation des tests sans dépendre de l'affichage
 *
 */
public class MainActivityViewModel extends AndroidViewModel {

    /**
     * value1 et value2 sont les deux paramètres en écriture, il sont donc déclarés
     * en tant que "Mutable"LiveData, qui permet la modification de la valeur.
     */
    MutableLiveData<Integer> value1 = new MutableLiveData<>();
    MutableLiveData<Integer> value2 = new MutableLiveData<>();
    /**
     * result est lui un paramètre en lecture seule, dont la valeur va dépendre de value1 et value2.
     * Il est donc déclaré en tant que "Mediator"LiveData, qui permet la réconciliation de deux
     * LiveData.
     */
    MediatorLiveData<Integer> result = new MediatorLiveData<>();

    /**
     * resultString est la version exposée à la vue de result, formatée pour être affichée
     * dans un champ de type texte.
     * Le type déclaré ici est LiveData, car il ne sera pas nécessaire de faire des modifications
     * après son initialisation. L'initialisation est ici faite avec la méthode map de
     * Transformations, cela permet de créer une image de result, après lui avoir appliquer la
     * modification demandée, ici une transformation en String.
     */
    LiveData<String> resultString = Transformations.map(result,input -> {
        if(input != null) {
            return "Résultat : " + input;
        }
        return "";
    });

    /**
     * Je garde une référfence vers le Dao pour faire des modifications sur la base de données
     * dès que nécessaire.
     */
    ResultatDao resultatDao = null;

    /**
     * Constructeur du ViewModel, il est automatiquement appelé par le système via un
     * ViewModelProvider.
     * @param application l'application.
     */
    public MainActivityViewModel(Application application) {
        super(application);

        // ----------------------------------------------------
        // - Initialisation de la base de données
        Executors.newSingleThreadExecutor().submit(() -> {
            AppDatabase db = Room
                    .databaseBuilder(getApplication(),AppDatabase.class,"resultats")
                    .build();
            // - Récupération du Dao résultats
            resultatDao = db.getResultatDao();
        });
        // ----------------------------------------------------

        /**
         * On configure ici notre MediatorLiveData pour qu'il soit abonné à value1 et value2.
         * De cette façon, dès qu'une modification est apporté à value1 ou value2, l'observer
         * ci-dessous est appelé, et le résultat de la somme de value1 et value2 est placé dans
         * result.
         */
        Observer<Integer> integerObserver = integer -> {
            addValues();
        };
        result.addSource(value1, integerObserver);
        result.addSource(value2, integerObserver);
    }

    /**
     * Effectue l'ajout des valeurs, publie le résultat dans result et stocke l'opération
     * dans l'historique local des opérations
     */
    private void addValues(){
        //On récupère ici la valeur stockée dans value1 et dans value2.
        // - Avec les LiveData, il faut toujours tester si la valeur est nulle ou non,
        // car un LiveData qui n'est pas observé n'aura pas de valeur.
        Integer v1 = value1.getValue() != null ? value1.getValue() : 0;
        Integer v2 = value2.getValue() != null ? value2.getValue() : 0;
        //result étant un MediatorLiveData, il est possible de lui affecter une valeur,
        //en effet, MediatorLiveData hérite de MutableLiveData.
        result.setValue(v1 + v2);

        /**
         * On stocke ici l'opréation dans l'historique, en base de données.
         * - Le code de sauvegarde de la base de données est encapsulé dans un appel sur un
         * thread en tâche de fond grâce aux Executors.
         */
        Executors.newSingleThreadExecutor().submit(() -> {
            if (resultatDao != null) {
                // - Ajoute un résultat dans la base de données
                // On crée un résultat en lui donnant les valeurs de l'opération que l'on
                // vient d'effectuer.
                Resultat resultat = new Resultat();
                resultat.setId(0L);
                resultat.setResultat(v1 + v2);
                resultat.setValeur1(v1);
                resultat.setValeur2(v2);
                // On appelle la méthode add, et l'identifiant de l'élément créé est retourné
                long idResultat = resultatDao.add(resultat);
                // Pour les besoins de démo, on affiche le résultat de l'opération d'ajout dans
                // les logs
                Log.d("DEBUG", "résultat ajouté avec l'identifiant suivant : " + idResultat);
            }
        });
    }

    /**
     * Affecte la valeur 1 de l'opération
     * @param value1
     */
    public void setValue1(Integer value1) {
        //On affecte ici la valeur dans le MutableLiveData
        this.value1.setValue(value1);
    }

    /**
     * Affecte la valeur 2 de l'opération
     * @param value2
     */
    public void setValue2(Integer value2) {
        this.value2.setValue(value2);
    }

    /**
     * Retourne le résultat observable de l'opération
     * On retourne ici un LiveData, car on ne veut pas permettre à la vue de faire des modifications
     * sur le résultat.
     * @return
     */
    public LiveData<String> getResultString() {
        return resultString;
    }
}
