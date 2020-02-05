package br.com.envolvedesenvolve.casalemcasa.View;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import br.com.envolvedesenvolve.casalemcasa.R;

public class AboutFragment extends DialogFragment {

    String pathError;

    static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        pathError = getArguments().getString("EXTRA_PATHFILE_ERROR");

        PackageManager manager = getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        View tv = v.findViewById(R.id.textView);
        ((TextView) tv).setText("2020 "
                + "\n"
                + "\nPackageName = " + info.packageName
                + "\nVersão = " + info.versionName
                + "\n"
                + "\nCaminho da pasta = " + (Environment.getExternalStoragePublicDirectory("nomePasta") + "/nomeDoArquivo.csv")
                + "\nPATH = " + pathError
        );

        return v;
    }
}
