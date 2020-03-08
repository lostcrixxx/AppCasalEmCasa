package br.com.envolvedesenvolve.casalemcasa.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import br.com.envolvedesenvolve.casalemcasa.R;

/**
 * Created by Cristiano M. on 07/03/2020
 * Modified by Cristiano M. on 07/03/2020
 */

public class QrCodeFragment extends DialogFragment {

    private String code;

    private TextView txtCode;
    private SharedPreferences prefs;
    private ImageView ivQRCode;

    public static QrCodeFragment newInstance() {
        return new QrCodeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qrcode, container, false);

        txtCode = v.findViewById(R.id.txtCode);
        ivQRCode = (ImageView) v.findViewById(R.id.ivQRCode);

        prefs = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        code = prefs.getString("codePrefs", "");
        txtCode.setText(code);

        //                private void gerarQRCode() {
        String texto = code;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(texto, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQRCode.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }


        return v;
    }
}
