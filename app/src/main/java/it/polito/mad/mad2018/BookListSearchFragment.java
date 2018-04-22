package it.polito.mad.mad2018;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.utils.BookListAdapter;
import it.polito.mad.mad2018.utils.FragmentDialog;

public class BookListSearchFragment extends FragmentDialog<BookListSearchFragment.DialogID>{

    public static BookListSearchFragment newInstance(@NonNull List<Book> books) {
        BookListSearchFragment fragment = new BookListSearchFragment();
        Bundle args = new Bundle();
        ArrayList<Book> bookList = new ArrayList<>(); // arrayList is serializable
        for(Book book : books)
            bookList.add(book);
        //args.putSerializable(Book.BOOK_INFO_KEY, bookList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_book_search_results, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fbs_book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BookListAdapter(new ArrayList<>()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getActivity() != null;
        assert getView() != null;
        assert getArguments() != null;

        //final ArrayList<Book> bookList = (ArrayList<Book>) getArguments().getSerializable(Book.BOOK_INFO_KEY);

        // TODO: change title of the activity
        getActivity().setTitle("Results");

    }

    public enum DialogID {
        DIALOG_LOADING,
        DIALOG_SAVING,
        DIALOG_ADD_PICTURE,
    }
}