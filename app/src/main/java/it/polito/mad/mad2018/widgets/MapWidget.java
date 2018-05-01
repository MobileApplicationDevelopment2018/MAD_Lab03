package it.polito.mad.mad2018.widgets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.AlgoliaResultsListener;
import com.algolia.instantsearch.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.Query;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapWidget implements OnMapReadyCallback, AlgoliaSearcherListener, AlgoliaResultsListener {
    private static final String TAG = "MapWidget";
    @NonNull
    final SupportMapFragment mapFragment;
    public GoogleMap googleMap;
    @NonNull
    private List<JSONObject> hits = new ArrayList<>();

    public MapWidget(@NonNull final SupportMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMapPOIs();
    }

    @Override
    public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        addHits(results, !isLoadingMore);
        if (googleMap != null) {
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    updateMapPOIs();
                }
            });
        }
    }

    /**
     * Adds or replaces hits to/in this widget.
     *
     * @param results     A {@link JSONObject} containing hits.
     * @param isReplacing {@code true} if the given hits should replace the current hits.
     */
    private void addHits(@Nullable SearchResults results, boolean isReplacing) {
        if (results == null) {
            if (isReplacing) {
                hits.clear();
            }
            return;
        }
        final JSONArray newHits = results.hits;
        if (isReplacing) {
            hits.clear();
        }
        for (int i = 0; i < newHits.length(); ++i) {
            final JSONObject hit = newHits.optJSONObject(i);
            if (hit != null) {
                hits.add(hit);
            }
        }
    }

    private void updateMapPOIs() {
        googleMap.clear();
        if (hits.isEmpty())
            return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng location = new LatLng(45.116177, 7.742615);
        MarkerOptions marker = new MarkerOptions().position(location);
        for (final JSONObject hit : hits) {
            location = new LatLng(45.116177, 7.742615);
            marker = new MarkerOptions().position(location);
            try {
                location = new LatLng(hit.getJSONObject("_geoloc").getDouble("lat"), hit.getJSONObject("_geoloc").getDouble("lon"));
                marker = new MarkerOptions().position(location).title(hit.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            builder.include(marker.getPosition());
            googleMap.addMarker(marker);
        }

        LatLngBounds bounds = builder.build();
        // update the camera
        int padding = 10;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }

    @Override
    public void initWithSearcher(@NonNull Searcher searcher) {
        searcher.setQuery(searcher.getQuery().setAroundRadius(Query.RADIUS_ALL));
    }
}