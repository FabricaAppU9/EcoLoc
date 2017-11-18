package br.com.fabappu9.ecoloc;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Guilherme on 18/11/2017.
 */

public class Localizador{

    public static String encontrarEndereco(Context context,double latitude, double longitude){
        try{
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            return address;

        } catch (IOException ex){
            return "";
        }
    }
}
