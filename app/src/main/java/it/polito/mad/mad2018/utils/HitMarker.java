package it.polito.mad.mad2018.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class HitMarker {
    public static MarkerOptions marker(final JSONObject jsonObject) {
        final MarkerOptions marker = new MarkerOptions();
        try {
            marker.title(jsonObject.getString("name"));
        } catch (JSONException e) {
        }
        try {
            final String location = jsonObject.getString("smart_location");
            final String price = jsonObject.getString("price_formatted");
            marker.snippet(price + " - " + location);
        } catch (JSONException e) {
        }
        try {
            final Double latitude = jsonObject.getDouble("lat");
            final Double longitude = jsonObject.getDouble("lng");
            marker.position(new LatLng(latitude, longitude));
        } catch (JSONException e) {
        }

        return marker;
    }
}
