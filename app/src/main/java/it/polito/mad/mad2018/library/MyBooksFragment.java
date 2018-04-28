package it.polito.mad.mad2018.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import it.polito.mad.mad2018.BookInfoActivity;
import it.polito.mad.mad2018.BookInfoFragment;
import it.polito.mad.mad2018.R;
import it.polito.mad.mad2018.data.Book;

public class MyBooksFragment extends Fragment {

    private FirebaseRecyclerAdapter<Book, BookAdapter.BookHolder> adapter;

    public MyBooksFragment() { /* Required empty public constructor */ }

    public static MyBooksFragment newInstance() {
        return new MyBooksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_books, container, false);
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;

        final FloatingActionButton floatingActionButton = view.findViewById(R.id.fmb_add_book);
        floatingActionButton.setOnClickListener(v -> {
            Intent toAddBook = new Intent(getActivity(), AddBookActivity.class);
            startActivity(toAddBook);
        });

        View noBooksView = view.findViewById(R.id.fmb_no_books);
        RecyclerView recyclerView = view.findViewById(R.id.fmb_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Book> options = Book.getBooksLocalUser();
        adapter = new BookAdapter(options, (v, model) -> {
            Intent toBookInfo = new Intent(getActivity(), BookInfoActivity.class);
            toBookInfo.putExtra(Book.BOOK_KEY, model);
            toBookInfo.putExtra(BookInfoFragment.BOOK_DELETABLE_KEY, true);
            startActivity(toBookInfo);
        }, (count) -> {
            noBooksView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }
}
