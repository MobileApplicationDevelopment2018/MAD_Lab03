package it.polito.mad.mad2018.library;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

import it.polito.mad.mad2018.AddBookFragment;
import it.polito.mad.mad2018.R;
import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.data.UserProfile;
import it.polito.mad.mad2018.utils.BookHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBooksFragment extends Fragment {
    private List<Book> myBooks;

    public MyBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBooksFragment newInstance(@NonNull UserProfile profile) {
        MyBooksFragment fragment = new MyBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_books, container, false);

        final FloatingActionButton floatingActionButton = view.findViewById(R.id.l_add_book);
        floatingActionButton.setOnClickListener(v -> getFragmentManager().beginTransaction()
                                                        .addToBackStack(null)
                                                        .replace(R.id.content_frame, AddBookFragment.newInstance())
                                                        .commit());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fmb_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        FirebaseRecyclerOptions<Book> options = Book.getBooksLocalUser();
        FirebaseRecyclerAdapter<Book, BookHolder> adapter =
                new FirebaseRecyclerAdapter<Book, BookHolder>(options) {
                    @NonNull
                    @Override
                    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.book_library_item, parent, false);
                        return new BookHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
                        holder.setBookTitle(model.getTitle());
                        holder.setBookImage(model.getBookPictureReference(), getContext());
                    }
                };

        recyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
