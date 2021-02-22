package lpmi.potherat.promo2021.mvvmdemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.Executors;

import lpmi.potherat.promo2021.mvvmdemoapp.dao.ResultatDao;
import lpmi.potherat.promo2021.mvvmdemoapp.database.AppDatabase;
import lpmi.potherat.promo2021.mvvmdemoapp.model.Resultat;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // J'initialise et récupère le viewmodel
        viewModel = new ViewModelProvider(this,
                // On utilise ici le deuxième paramètre pour fournir une Factory
                // qui servira à la création de notre MainActivityViewModel avec
                // l'application en paramètre
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
            ).get(MainActivityViewModel.class);

        // - J'observe le résultat string et affecte directement le résultat dans la textview
        viewModel.getResultString().observe(this,string -> {
            if(string != null) {
                ((TextView) findViewById(R.id.txtResultat)).setText(string);
            }
        });

        // - On affecte valeur 1 et valeur 2 dans le viewmodel lors du changement de la valeur
        // dans l'edittext
        ((EditText)findViewById(R.id.txtValeur1)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Rien à faire ici
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //On transforme le contenu texte en entier
                    Integer value1 = Integer.valueOf(charSequence.toString());
                    //On affecte la valeur à notre ViewModel.
                    viewModel.setValue1(value1);
                } catch (NumberFormatException ignored){}

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Rien à faire ici
            }
        });
        ((EditText)findViewById(R.id.txtValeur2)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Rien à faire ici
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    viewModel.setValue2(Integer.valueOf(charSequence.toString()));
                } catch (NumberFormatException ignored){}
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Rien à faire ici
            }
        });

    }
}