package it.polito.mad.mad2018.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;
import com.google.firebase.storage.FirebaseStorage;
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
        try {
            String bookId = result.getString("bookId");
            Log.d("Tag", bookId + "=>" + result.getString("title"));
            if (bookId != null) {
                StorageReference ref = FirebaseStorage.getInstance().getReference()
                        .child(Book.FIREBASE_STORAGE_BOOKS_FOLDER)
                        .child(bookId)
                        .child(Book.FIREBASE_STORAGE_IMAGE_NAME);

                if (ref != null) {
                    GlideApp.with(getContext())
                            .load(ref)
                            .into(this);
                }
            } else {
                GlideApp.with(getContext())
                        .load(R.drawable.default_book_preview)
                        .into(this);
            }

        } catch (JSONException e) {
            // Non existing field, don't do anything
            GlideApp.with(getContext())
                    .load(R.drawable.default_book_preview)
                    .into(this);
        }
    }
}
