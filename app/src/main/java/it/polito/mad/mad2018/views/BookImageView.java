package it.polito.mad.mad2018.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;

import org.json.JSONObject;

public class BookImageView extends AppCompatImageView implements AlgoliaHitView {
    public BookImageView(Context context) {
        super(context);
    }

    @Override
    public void onUpdateView(JSONObject result) {

    }
}
