package it.polito.mad.mad2018;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import it.polito.mad.mad2018.data.Book;

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

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bi_main_fragment, BookInfoFragment.newInstance(book), "bi_main_fragment");
        ft.commit();
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
}
