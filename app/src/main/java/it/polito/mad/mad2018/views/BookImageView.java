package it.polito.mad.mad2018.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import it.polito.mad.mad2018.R;
import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.utils.GlideApp;

public class BookImageView extends AppCompatImageView implements AlgoliaHitView {

    public BookImageView(Context context) {
        super(context);
    }

    public BookImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onUpdateView(JSONObject result) {

        StorageReference reference = null;
        try {
            String bookId = result.getString("bookId");
            if (bookId != null) {
                reference = Book.getBookThumbnailReference(bookId);
            }
        } catch (JSONException e) { /* do nothing */ }

        GlideApp.with(getContext())
                .load(reference)
                .placeholder(R.drawable.default_header)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this);
    }
}
