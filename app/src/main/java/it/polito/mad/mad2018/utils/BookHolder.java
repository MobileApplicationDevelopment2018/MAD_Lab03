package it.polito.mad.mad2018.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.StorageReference;

import it.polito.mad.mad2018.BookInfoFragment;
import it.polito.mad.mad2018.R;
import it.polito.mad.mad2018.data.Book;

public class BookHolder extends RecyclerView.ViewHolder {
    private final TextView mBookTitle;
    private final ImageView mBookPicture;
    private final Context ctx;
    private String bookId;

    public BookHolder(@NonNull View view) {
        super(view);
        mBookTitle = view.findViewById(R.id.bli_book_title);
        mBookPicture = view.findViewById(R.id.bli_book_picture);
        ctx = view.getContext();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Item clicked go to the display fragment
                Fragment bi = BookInfoFragment.newInstance(bookId);
                FragmentManager fm = null;
                if (ctx instanceof FragmentActivity) {
                    fm = ((FragmentActivity) ctx).getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.content_frame, bi)
                            .commit();
                }
            }
        });
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

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
