package it.polito.mad.mad2018.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.StorageReference;

import it.polito.mad.mad2018.R;
import it.polito.mad.mad2018.data.Book;

public class BookHolder extends RecyclerView.ViewHolder {
    private final TextView mBookTitle;
    private final ImageView mBookPicture;
    private final Context ctx;

    public BookHolder(@NonNull View view) {
        super(view);
        mBookTitle = view.findViewById(R.id.bli_book_title);
        mBookPicture = view.findViewById(R.id.bli_book_picture);
        ctx = view.getContext();
    }

    public void setBookTitle(String title) {
        mBookTitle.setText(title);
    }

    public void setBookImage(String bookId) {
        setBookImage(Book.getBookThumbnailReference(bookId), ctx);
    }

    /**
     * @param imageReference: this is the reference in storage database of Firebase, we need it to retrieve
     *                        the eventual book picture uploaded previously from the user owning the book.
     * @param context
     */
    public void setBookImage(StorageReference imageReference, Context context) {
        GlideApp.with(context)
                .load(imageReference)
                .placeholder(R.drawable.default_book_preview)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(mBookPicture);
    }
}
