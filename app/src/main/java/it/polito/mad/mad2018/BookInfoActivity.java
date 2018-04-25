package it.polito.mad.mad2018;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.StorageReference;

import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.utils.GlideApp;
import me.gujun.android.taggroup.TagGroup;

public class BookInfoActivity extends AppCompatActivity {

    public static final String BOOK_KEY = "book_key";
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        // Set the toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (savedInstanceState != null) {
            this.book = (Book) savedInstanceState.getSerializable(BOOK_KEY);
        } else {
            this.book = (Book) this.getIntent().getSerializableExtra(BOOK_KEY);
        }

        assert book != null;
        fillViews(this.book);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BOOK_KEY, book);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fillViews(@NonNull Book book) {
        TextView title = findViewById(R.id.fbi_book_title);
        TextView authors = findViewById(R.id.fbi_book_authors);
        TextView publisher = findViewById(R.id.fbi_book_publisher);
        TextView editionYear = findViewById(R.id.fbi_book_edition_year);
        TextView language = findViewById(R.id.fbi_book_language);
        TextView conditions = findViewById(R.id.fbi_book_conditions);
        ImageView bookPicture = findViewById(R.id.fbi_book_picture);
        TagGroup tagGroup = findViewById(R.id.fbi_book_tags);
        TagGroup bookOwner = findViewById(R.id.fbi_book_owner);

        title.setText(book.getTitle());
        authors.setText(book.getAuthors(","));
        publisher.setText(book.getPublisher());
        editionYear.setText(String.valueOf(book.getYear()));
        language.setText(book.getLanguage());
        conditions.setText(book.getConditions());
        tagGroup.setTags(book.getTags());
        bookOwner.setTags("Owner"); // TODO: request owner name to firebase

        StorageReference reference = Book.getBookThumbnailReference(book.getBookId());

        GlideApp.with(this)
                .load(reference)
                .placeholder(R.drawable.ic_default_book_preview)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bookPicture);
    }
}
