package br.com.envolvedesenvolve.casalemcasa.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import br.com.envolvedesenvolve.casalemcasa.R;

public class CodeFragment extends DialogFragment {

    private SharedPreferences prefs;

    public static CodeFragment newInstance() {
        return new CodeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_code, container, false);

        View tv = v.findViewById(R.id.textView);
        ((TextView) tv).setText("Código da Lista"
                + "\n"
//                + "\nLista = " + code
                + "\n"
        );
        return v;
    }
}
