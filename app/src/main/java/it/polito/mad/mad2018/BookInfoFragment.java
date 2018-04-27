package it.polito.mad.mad2018;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class BookInfoFragment extends Fragment {
    private Book book;
    private ValueEventListener profileListener;
    private TextView title, authors, publisher, editionYear, language, conditions, bookOwner;
    ;
    private TagGroup tagGroup;
    private ImageView bookPicture;
    private Button ownerProfileBtn;
    private UserProfile user;
    private View view;

    public BookInfoFragment() { /* Required empty public constructor */ }

    public static BookInfoFragment newInstance(Book book) {
        BookInfoFragment fragment = new BookInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(BookInfoActivity.BOOK_KEY, book);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        book = (Book) getArguments().getSerializable(BookInfoActivity.BOOK_KEY);
        getActivity().setTitle(R.string.book_details);
        assert book != null;

        view = inflater.inflate(R.layout.fragment_book_info, container, false);

        title = view.findViewById(R.id.fbi_book_title);
        authors = view.findViewById(R.id.fbi_book_authors);
        publisher = view.findViewById(R.id.fbi_book_publisher);
        editionYear = view.findViewById(R.id.fbi_book_edition_year);
        language = view.findViewById(R.id.fbi_book_language);
        conditions = view.findViewById(R.id.fbi_book_conditions);
        bookPicture = view.findViewById(R.id.fbi_book_picture);
        tagGroup = view.findViewById(R.id.fbi_book_tags);
        bookOwner = view.findViewById(R.id.fbi_book_owner);
        ownerProfileBtn = view.findViewById(R.id.fbi_show_profile_button);

        fillViews();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setOnProfileLoadedListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsetOnProfileLoadedListener();
    }

    private void fillViews() {
        title.setText(book.getTitle());
        authors.setText(book.getAuthors(","));
        publisher.setText(book.getPublisher());
        editionYear.setText(String.valueOf(book.getYear()));
        language.setText(book.getLanguage());
        conditions.setText(book.getConditions());
        tagGroup.setTags(book.getTags());

        StorageReference reference = Book.getBookThumbnailReference(book.getBookId());

        GlideApp.with(view.getContext())
                .load(reference)
                .placeholder(R.drawable.ic_default_book_preview)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bookPicture);

        ownerProfileBtn.setOnClickListener(v -> {
            if (bookOwner.getText() == null)
                return;

            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.bi_main_fragment, ShowProfileFragment.newInstance(user), "");
            ft.addToBackStack(null); // To allow to come back to the previous fragment when back is pressed
            ft.commit();
        });
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
        bookOwner.setText(owner);
    }

}
