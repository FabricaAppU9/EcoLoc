package br.com.fabappu9.ecoloc;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import br.com.fabappu9.ecoloc.DTO.PontoDto;
import br.com.fabappu9.ecoloc.network.APIClient;
import br.com.fabappu9.ecoloc.network.APILocation;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Geraldo on 06/06/2017.
 */

public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private LatLng latLng;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocation;
    private LocationRequest mLocationRequest;
    private static final String TAG = "MapaFragment";
    private Marker mMarker;
    private String cadastrarEstePonto = "Deseja cadastrar este ponto?";
    private String cadastrarSnippet = "";
    public static final int CONSTANTE_TELA_1 = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefEditor;
    private List<PontoDto> pontos;
    private APILocation location;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        sharedPreferences = getActivity().getSharedPreferences("jaLogouAntes", Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();

        Boolean jaLogouAntes = sharedPreferences.getBoolean("jaLogouAntes", false);

        if(jaLogouAntes != true){
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.mapa, container, false);

        return mView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);

        }

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        SpotsDialog dialog = new SpotsDialog(getContext());
        dialog.show();
        Call<List<PontoDto>> retorno = null;
        retorno = new APIClient().getRestService().getPontoDTO("12345", "GETPONTOS", "");
        configurarCallback(retorno,mGoogleMap);
       // APILocation location=new APILocation();
       // location.callLocalization();
        //mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-23.759310021069908, -46.79334104061127)).title("Minha ultima posição com sinal").snippet("Testando map fragment"));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-23.5489, -46.6388)).title("Minha ultima posição com sinal").snippet("Testando map fragment"));

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(-23.5489, -46.6388)).zoom(9).bearing(0).tilt(4).build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        dialog.dismiss();
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String clickCount;
                clickCount = marker.getTitle();

                if(clickCount != cadastrarEstePonto){

                    double latitude = marker.getPosition().latitude;
                    double longitude = marker.getPosition().longitude;

                    Bundle params = new Bundle();
                    params.putString("Endereco", Localizador.encontrarEndereco(getActivity(), latitude, longitude));
                    params.putDouble("Latitude",latitude);
                    params.putDouble("Longitude",longitude);
                    Intent intent = new Intent(getActivity(), InfoEnderecoActivity.class);

                    intent.putExtras(params);

                    startActivityForResult(intent, CONSTANTE_TELA_1 );

                }

            }
        });

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if(mMarker != null){
                    mMarker.remove();
                    Log.d(TAG, "onMapClick: ["+latLng.latitude +"/"+latLng.longitude +"]");
                    cadastrarSnippet = Localizador.encontrarEndereco(getActivity(), latLng.latitude, latLng.longitude);
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(cadastrarEstePonto).snippet(cadastrarSnippet));
                }else {
                    Log.d(TAG, "onMapClick: ["+latLng.latitude +"/"+latLng.longitude +"]");
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

    private void configurarCallback(Call<List<PontoDto>> retorno,GoogleMap map ) {

        retorno.enqueue(new Callback<List<PontoDto>>() {
            @Override
            public void onResponse(Call<List<PontoDto>> call, Response<List<PontoDto>> response) {
                if (!response.isSuccessful()) {
                    Log.e("ERRO:", response.message());
                } else {

                    pontos =  response.body();
                    for(int i=0;i<pontos.size();i++){

                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(pontos.get(i).getLatitude()),Double.parseDouble(pontos.get(i).getLongitude())))
                                .title(pontos.get(i).getDescricao()).snippet(pontos.get(i).getTipoMaterial()))
                        ;
                    }

                }
            }

            @Override
            public void onFailure(Call<List<PontoDto>> call, Throwable error) {
                //Toast.makeText(MapaFragment.this, "Deu Ruim: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
