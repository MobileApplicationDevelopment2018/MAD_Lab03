package it.polito.mad.mad2018.library;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad.mad2018.R;

public class BorrowedBooksFragment extends Fragment {

    public BorrowedBooksFragment() { /* Required empty public constructor */ }

    public static BorrowedBooksFragment newInstance() {
        BorrowedBooksFragment fragment = new BorrowedBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrowed_books, container, false);
    }
}
