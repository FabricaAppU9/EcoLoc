package br.com.fabappu9.ecoloc;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import br.com.fabappu9.ecoloc.DTO.PontoDto;
import br.com.fabappu9.ecoloc.network.APIClient;
import br.com.fabappu9.ecoloc.network.APILocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Geraldo on 06/06/2017.
 */

public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private RetainedFragment mapWorkFragment;
    private MapView mMapView;
    private Marker mMarker, mCurrLocation;
    private String cadastrarEstePonto = "Deseja cadastrar este ponto?";
    private String cadastrarSnippet = "";
    private static GoogleMap mGoogleMap;

    private View mView;
    private LatLng latLng;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = "MapaFragment";

    public CameraPosition cameraGoogle = null;

    public static final int CONSTANTE_TELA_1 = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefEditor;
    private APILocation location;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        RetainedFragment fragment = (RetainedFragment) getFragmentManager().findFragmentByTag("work");

        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.mapa, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // --- cria fragment para salvar os pontos e camera ---
        FragmentManager fm = getFragmentManager();
        mapWorkFragment = (RetainedFragment)fm.findFragmentByTag("work");

        if (mapWorkFragment == null) {
            mapWorkFragment = new RetainedFragment();
            mapWorkFragment.setTargetFragment(this, 0);
            fm.beginTransaction().add(mapWorkFragment, "work").commit();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        sharedPreferences = getActivity().getSharedPreferences("jaLogouAntes", Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();

        // --- tela info instruções de uso ---
        Boolean jaLogouAntes = sharedPreferences.getBoolean("jaLogouAntes", false);
        if(!jaLogouAntes){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Instuções")
                    .setView(inflater.inflate(R.layout.alert_dialog,null))
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Boolean conferindo = sharedPrefEditor.putBoolean("jaLogouAntes", true).commit();
                            Log.d(TAG, "onClick: "+ conferindo);
                        }
                    });
            builder.show();
        }


        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
        mapWorkFragment.cameraGoogle = mGoogleMap.getCameraPosition();
    }


    // --- Mapa ---
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().isZoomControlsEnabled();
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        mapWorkFragment.configurarCallback(mGoogleMap);

        // APILocation location=new APILocation();
        // location.callLocalization();
        //mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-23.759310021069908, -46.79334104061127)).title("Minha ultima posição com sinal").snippet("Testando map fragment"));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-23.5489, -46.6388)).title("Minha ultima posição com sinal").snippet("Testando map fragment"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mapWorkFragment.cameraGoogle));
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String clickCount;
                clickCount = marker.getTitle();

                if (!clickCount.equals(cadastrarEstePonto)) {

                    double latitude = marker.getPosition().latitude;
                    double longitude = marker.getPosition().longitude;

                    Bundle params = new Bundle();
                    params.putString("Endereco", Localizador.encontrarEndereco(getActivity(), latitude, longitude));
                    params.putDouble("Latitude", latitude);
                    params.putDouble("Longitude", longitude);
                    Intent intent = new Intent(getActivity(), InfoEnderecoActivity.class);

                    intent.putExtras(params);

                    startActivityForResult(intent, CONSTANTE_TELA_1);
                }
            }
        });

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (mMarker != null) {
                    mMarker.remove();
                    Log.d(TAG, "onMapClick: [" + latLng.latitude + "/" + latLng.longitude + "]");
                    cadastrarSnippet = Localizador.encontrarEndereco(getActivity(), latLng.latitude, latLng.longitude);
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(cadastrarEstePonto).snippet(cadastrarSnippet));
                } else {
                    Log.d(TAG, "onMapClick: [" + latLng.latitude + "/" + latLng.longitude + "]");
                    cadastrarSnippet = Localizador.encontrarEndereco(getActivity(), latLng.latitude, latLng.longitude);
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(cadastrarEstePonto).snippet(cadastrarSnippet));
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    private void createNoGpsDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        break;
                }
            }
        };

        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //mNoGpsDialog = builder.setMessage("Por favor ative seu GPS para usar esse aplicativo.")
          //      .setPositiveButton("Ativar", dialogClickListener)
            //    .create();
        //mNoGpsDialog.show();

    }



    public static class RetainedFragment extends Fragment {
        protected CameraPosition cameraGoogle = null;
        private List<PontoDto> pontos =null;
        Call<List<PontoDto>> retorno = null;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            cameraGoogle = CameraPosition.builder().target(new LatLng(-23.5489, -46.6388)).zoom(15).bearing(0).tilt(4).build();
        }


        private void iniCallback(final GoogleMap mGoogleMap){
            retorno = new APIClient().getRestService().getPontoDTO("12345", "GETPONTOS", "");
            retorno.enqueue(new Callback<List<PontoDto>>() {
                @Override
                public void onResponse(Call<List<PontoDto>> call, Response<List<PontoDto>> response) {
                    if (!response.isSuccessful()) {
                        Log.e("ERRO:", response.message());
                    } else {
                        pontos = response.body();
                        configurarCallback(mGoogleMap);
                    }
                }
                @Override
                public void onFailure(Call<List<PontoDto>> call, Throwable error) {
                    //Toast.makeText(MapaFragment.this, "Deu Ruim: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        // ---- carregamento no mapa -----
        private void configurarCallback(GoogleMap mGoogleMap) {
            if (pontos != null) {
                for (int i = 0; i < pontos.size(); i++) {
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(pontos.get(i).getLatitude()), Double.parseDouble(pontos.get(i).getLongitude())))
                            .title(pontos.get(i).getDescricao()).snippet(pontos.get(i).getTipoMaterial()));
                }
            }else
                iniCallback(mGoogleMap);
        }

    }
}