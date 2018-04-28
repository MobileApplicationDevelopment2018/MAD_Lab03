package it.polito.mad.mad2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.views.Hits;

import org.json.JSONException;

import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.data.Constants;

public class ExploreFragment extends Fragment {

    private Searcher searcher;
    private InstantSearch helper;
    private AppBarLayout appBarLayout;
    private LinearLayout searchViewLayout;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        searchViewLayout = (LinearLayout) inflater.inflate(R.layout.search_view_layout, null);

        setHitsOnClickListener(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getActivity() != null;
        getActivity().setTitle(R.string.explore);

        appBarLayout = getActivity().findViewById(R.id.app_bar_layout);
        appBarLayout.addView(searchViewLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        RelativeLayout filters = searchViewLayout.findViewById(R.id.search_filters_bar);
        searchViewLayout.findViewById(R.id.show_search_options)
                .setOnClickListener(v -> filters.setVisibility(filters.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));

        searcher = Searcher.create(Constants.ALGOLIA_APP_ID, Constants.ALGOLIA_SEARCH_API_KEY,
                Constants.ALGOLIA_INDEX_NAME);
        helper = new InstantSearch(this.getActivity(), searcher);
        //NumericSelector numericSelector = getView().findViewById(R.id.numeric_selector);
        //numericSelector.initWithSearcher(searcher);
        helper.search();

    }

    @Override
    public void onStop() {
        super.onStop();
        searcher.destroy();
    }

    @Override
    public void onDetach() {
        if (appBarLayout != null) {
            appBarLayout.removeView(searchViewLayout);
        }
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_explore, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //helper.registerSearchView(this.getActivity(), menu, R.id.explore_search);
        //helper.registerWidget(getView().findViewById(R.id.algolia_hits));
    }

    private void setHitsOnClickListener(View view) {

        final Hits hits = view.findViewById(R.id.algolia_hits);
        hits.setOnItemClickListener((recyclerView, position, v) -> {

            try {
                String bookId = hits.get(position).getString(Book.ALGOLIA_BOOK_ID_KEY);
                if (bookId != null) {
                    Intent toBookInfo = new Intent(getActivity(), BookInfoActivity.class);
                    toBookInfo.putExtra(Book.BOOK_ID_KEY, bookId);
                    startActivity(toBookInfo);
                    return;
                }
            } catch (JSONException e) { /* Do nothing */ }

            Toast.makeText(getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
        });
    }
}
