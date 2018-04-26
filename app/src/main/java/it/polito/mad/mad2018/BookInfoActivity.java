package it.polito.mad.mad2018;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.data.UserProfile;
import it.polito.mad.mad2018.utils.GlideApp;
import me.gujun.android.taggroup.TagGroup;

public class BookInfoActivity extends AppCompatActivity {

    public static final String BOOK_KEY = "book_key";
    private Book book;
    private ValueEventListener profileListener;
    private TextView title, authors, publisher, editionYear, language, conditions;
    private TagGroup tagGroup, bookOwner;
    private ImageView bookPicture;
    private Button ownerProfileBtn;
    private UserProfile user;

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

        findViews();
        fillViews(this.book);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnProfileLoadedListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsetOnProfileLoadedListener();
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
        title = findViewById(R.id.fbi_book_title);
        authors = findViewById(R.id.fbi_book_authors);
        publisher = findViewById(R.id.fbi_book_publisher);
        editionYear = findViewById(R.id.fbi_book_edition_year);
        language = findViewById(R.id.fbi_book_language);
        conditions = findViewById(R.id.fbi_book_conditions);
        bookPicture = findViewById(R.id.fbi_book_picture);
        tagGroup = findViewById(R.id.fbi_book_tags);
        bookOwner = findViewById(R.id.fbi_book_owner);

        title.setText(book.getTitle());
        authors.setText(book.getAuthors(","));
        publisher.setText(book.getPublisher());
        editionYear.setText(String.valueOf(book.getYear()));
        language.setText(book.getLanguage());
        conditions.setText(book.getConditions());
        tagGroup.setTags(book.getTags());

        StorageReference reference = Book.getBookThumbnailReference(book.getBookId());

        GlideApp.with(this)
                .load(reference)
                .placeholder(R.drawable.ic_default_book_preview)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bookPicture);
    }

    private void findViews() {
        title = findViewById(R.id.fbi_book_title);
        authors = findViewById(R.id.fbi_book_authors);
        publisher = findViewById(R.id.fbi_book_publisher);
        editionYear = findViewById(R.id.fbi_book_edition_year);
        language = findViewById(R.id.fbi_book_language);
        conditions = findViewById(R.id.fbi_book_conditions);
        bookPicture = findViewById(R.id.fbi_book_picture);
        tagGroup = findViewById(R.id.fbi_book_tags);
        bookOwner = findViewById(R.id.fbi_book_owner);
        ownerProfileBtn = findViewById(R.id.fbi_show_profile_button);
    }

    private void setOnProfileLoadedListener() {
        if (book.getOwnerID() == null)
            return;

        this.profileListener = UserProfile.setOnProfileLoadedListener(
                this.book.getOwnerID(),
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!unsetOnProfileLoadedListener()) {
                            return;
                        }

                        UserProfile.Data data = dataSnapshot.getValue(UserProfile.Data.class);
                        if (data == null) {
                        } else {
                            user = new UserProfile(data, getResources());
                            updateOwnerId(user.getUsername());
                            Log.d("User Owner", user.getUsername());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (!unsetOnProfileLoadedListener()) {
                            return;
                        }
                    }
                });
    }

    private boolean unsetOnProfileLoadedListener() {
        if (this.profileListener != null) {
            UserProfile.unsetOnProfileLoadedListener(book.getOwnerID(), this.profileListener);
            this.profileListener = null;
            return true;
        }
        return false;
    }

    private void updateOwnerId(String owner) {
        bookOwner.setTags(owner);
    }
}
