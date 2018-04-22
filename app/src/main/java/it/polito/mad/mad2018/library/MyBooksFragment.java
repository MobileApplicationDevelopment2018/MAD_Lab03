package it.polito.mad.mad2018.library;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad.mad2018.AddBookFragment;
import it.polito.mad.mad2018.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBooksFragment extends Fragment {

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
    public static MyBooksFragment newInstance() {
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
                                                        .replace(R.id.content_frame, AddBookFragment.newInstance())
                                                        .commit());

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
