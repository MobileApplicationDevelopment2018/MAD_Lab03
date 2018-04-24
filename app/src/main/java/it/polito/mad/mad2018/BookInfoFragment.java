package it.polito.mad.mad2018;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.utils.GlideApp;
import me.gujun.android.taggroup.TagGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookInfoFragment newInstance(String bookId) {
        BookInfoFragment fragment = new BookInfoFragment();
        Bundle args = new Bundle();
        args.putString("bookId", bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String bookId = (String) getArguments().getString("bookId");

        DatabaseReference bookDbRef = Book.getBookByLocalUser(bookId);
        bookDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Book.Data data = dataSnapshot.getValue(Book.Data.class);
                Book book = new Book(bookId, data);
                fillViews(book);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fillViews(Book book) {
        assert getView() != null;
        assert book != null;

        TextView title = getView().findViewById(R.id.fbi_book_title);
        TextView authors = getView().findViewById(R.id.fbi_book_authors);
        TextView publisher = getView().findViewById(R.id.fbi_book_publisher);
        TextView editionYear = getView().findViewById(R.id.fbi_book_edition_year);
        TextView language = getView().findViewById(R.id.fbi_book_language);
        TextView conditions = getView().findViewById(R.id.fbi_book_conditions);
        ImageView bookPicture = getView().findViewById(R.id.fbi_book_picture);
        TagGroup tagGroup = getView().findViewById(R.id.fbi_book_tags);
        TagGroup bookOwner = getView().findViewById(R.id.fbi_book_owner);


        title.setText(book.getTitle());
        authors.setText(book.getAuthors(","));
        publisher.setText(book.getPublisher());
        editionYear.setText(String.valueOf(book.getYear()));
        language.setText(book.getLanguage());
        conditions.setText(book.getConditions());
        tagGroup.setTags(book.getTags());
        bookOwner.setTags("Owner"); // TODO: request owner name to firebase

        StorageReference reference = Book.getBookThumbnailReference(book.getBookId());

        GlideApp.with(getContext())
                .load(reference)
                .placeholder(R.drawable.default_book_preview)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bookPicture);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
