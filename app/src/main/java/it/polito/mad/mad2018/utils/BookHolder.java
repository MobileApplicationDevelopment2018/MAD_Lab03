package it.polito.mad.mad2018.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import it.polito.mad.mad2018.R;

public class BookHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    private final TextView mBookTitle, mBookAuthor;
    private final ImageView mBookPicture;

    public BookHolder(@NonNull View view) {
        super(view);
        mBookTitle = view.findViewById(R.id.fbs_book_item_title);
        mBookAuthor = view.findViewById(R.id.fbs_book_item_author);
        mBookPicture = view.findViewById(R.id.fbs_book_item_image);
    }

    public void setBookTitle(String title) {
        mBookTitle.setText(title);
    }

    public void setBookAuthor(String author) {
        mBookAuthor.setText(author);
    }

    /**
     * @param imageReference:   this is the reference in storage database of Firebase, we need it to retrieve
     *                 the eventual book picture uploaded previously from the user owning the book.
     * @param context:
     */
    public void setBookImage(StorageReference imageReference, Context context) {
        if (imageReference != null) {
            GlideApp.with(context)
                    .load(imageReference)
                    .into(mBookPicture);
        } else {
            // TODO: use some default picture.
        }
    }

    @Override
    public void onClick(View v) {

    }
}
