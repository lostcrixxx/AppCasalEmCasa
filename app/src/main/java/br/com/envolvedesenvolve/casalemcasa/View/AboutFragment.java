package br.com.envolvedesenvolve.casalemcasa.View;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import br.com.envolvedesenvolve.casalemcasa.R;

public class AboutFragment extends DialogFragment {

    public static AboutFragment newInstance(String some_title) {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        PackageManager manager = getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        View tv = v.findViewById(R.id.textView);
        ((TextView) tv).setText("2020 @ Envolve & Desenvolve"
                + "\n"
                + "\nApp = Casal em Casa"
                + "\nVersão = " + info.versionName
                + "\n"
        );
        return v;
    }
}
