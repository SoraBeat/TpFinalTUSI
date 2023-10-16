package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import com.example.tpfinaltusi.R;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar osmdroid (debe hacerse antes de cargar el diseño)
        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));

        setContentView(R.layout.activity_maps);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mapView = findViewById(R.id.map);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK); // Usa la fuente de mapas de Mapnik (OpenStreetMap)
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
        mapController.setZoom(15.0); // Establece el nivel de zoom inicial

        double latitud = getIntent().getDoubleExtra("latitud", 0);
        double longitud = getIntent().getDoubleExtra("longitud", 0);
        String titulo = getIntent().getStringExtra("tagmaps");

        // Añade un marcador
        GeoPoint location = new GeoPoint(latitud, longitud);
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(location);
        startMarker.setTitle(titulo);
        mapView.getOverlays().add(startMarker);

        // Centra el mapa en la ubicación
        mapController.setCenter(location);
    }
}
