package it.polito.mad.mad2018;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import it.polito.mad.mad2018.data.Book;
import it.polito.mad.mad2018.data.UserProfile;
import it.polito.mad.mad2018.utils.FragmentDialog;
import it.polito.mad.mad2018.utils.GlideApp;
import it.polito.mad.mad2018.utils.GlideRequest;
import it.polito.mad.mad2018.utils.Utilities;
import me.gujun.android.taggroup.TagGroup;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupStateListener;

public class BookInfoFragment extends FragmentDialog<BookInfoFragment.DialogID>
        implements PopupInflaterListener, PopupStateListener {

    public final static String BOOK_SHOW_OWNER_KEY = "book_show_owner_key";
    public final static String BOOK_DELETABLE_KEY = "book_deletable_key";

    private Book book;
    private UserProfile owner;

    private ValueEventListener profileListener;
    private LongPressPopup popup;
    private ImageView popupImage;

    public BookInfoFragment() { /* Required empty public constructor */ }

    public static BookInfoFragment newInstance(Book book, boolean showOwner, boolean deletable) {
        showOwner = showOwner && !UserProfile.isLocal(book.getOwnerID());
        deletable = deletable && !showOwner;

        BookInfoFragment fragment = new BookInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(Book.BOOK_KEY, book);
        args.putBoolean(BOOK_SHOW_OWNER_KEY, showOwner);
        args.putBoolean(BOOK_DELETABLE_KEY, deletable);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        assert getActivity() != null;
        getActivity().setTitle(R.string.book_details);

        assert getArguments() != null;
        book = (Book) getArguments().getSerializable(Book.BOOK_KEY);
        boolean showOwner = getArguments().getBoolean(BOOK_SHOW_OWNER_KEY);
        boolean deletable = getArguments().getBoolean(BOOK_DELETABLE_KEY);
        assert book != null;

        if (savedInstanceState != null) {
            owner = (UserProfile) savedInstanceState.getSerializable(UserProfile.PROFILE_INFO_KEY);
        }

        View view = inflater.inflate(R.layout.fragment_book_info, container, false);

        fillViewsBook(view);
        if (showOwner || deletable) {
            fillViewsOwner(view, showOwner);
            fillViewsDelete(view, deletable);
        } else {
            view.findViewById(R.id.fbi_extra).setVisibility(View.GONE);
            view.findViewById(R.id.fbi_line_extra).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setOnProfileLoadedListener();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (popup != null) {
            popup.unregister();
            popup.dismissNow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsetOnProfileLoadedListener();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(UserProfile.PROFILE_INFO_KEY, owner);
    }

    private void fillViewsBook(View view) {
        String unknown = getString(R.string.unknown);

        TextView isbn = view.findViewById(R.id.fbi_book_isbn);
        TextView title = view.findViewById(R.id.fbi_book_title);
        TextView authors = view.findViewById(R.id.fbi_book_authors);
        TextView publisher = view.findViewById(R.id.fbi_book_publisher);
        TextView editionYear = view.findViewById(R.id.fbi_book_edition_year);
        TextView language = view.findViewById(R.id.fbi_book_language);
        TextView conditions = view.findViewById(R.id.fbi_book_conditions);

        ImageView bookThumbnail = view.findViewById(R.id.fbi_book_picture);
        TagGroup tagGroup = view.findViewById(R.id.fbi_book_tags);

        isbn.setText(book.getIsbn());
        title.setText(book.getTitle());
        authors.setText(book.getAuthors(", "));
        publisher.setText(Utilities.isNullOrWhitespace(book.getPublisher()) ? unknown : book.getPublisher());
        editionYear.setText(String.valueOf(book.getYear()));
        language.setText(Utilities.isNullOrWhitespace(book.getLanguage()) ? unknown : book.getLanguage());
        conditions.setText(book.getConditions());

        List<String> tags = book.getTags();
        if (tags.size() == 0) {
            tags.add(getString(R.string.no_tags_found));
        }
        tagGroup.setTags(tags);

        GlideApp.with(view.getContext())
                .load(book.getBookThumbnailReferenceOrNull())
                .placeholder(R.drawable.ic_default_book_preview)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bookThumbnail);

        if (book.hasImage()) {
            popup = new LongPressPopupBuilder(getContext())
                    .setTarget(bookThumbnail)
                    .setPopupView(R.layout.popup_view_image, this)
                    .setPopupListener(this)
                    .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                    .build();

            popup.register();
        }
    }

    private void fillViewsOwner(View view, boolean showOwner) {
        View ownerView = view.findViewById(R.id.fbi_layout_owner);
        ownerView.setVisibility(showOwner ? View.VISIBLE : View.GONE);

        if (!showOwner) {
            return;
        }

        TextView ownerNameTextView = view.findViewById(R.id.fbi_book_owner);
        ProgressBar progressBarLoading = view.findViewById(R.id.fbi_loading_owner);
        Button ownerProfileButton = view.findViewById(R.id.fbi_show_profile_button);

        progressBarLoading.setVisibility(owner == null ? View.VISIBLE : View.GONE);
        ownerNameTextView.setVisibility(owner == null ? View.GONE : View.VISIBLE);
        ownerProfileButton.setEnabled(owner != null);

        if (owner != null) {
            ownerNameTextView.setText(owner.getUsername());
        }

        ownerProfileButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction()
                    .replace(R.id.bi_main_fragment, ShowProfileFragment.newInstance(owner, false))
                    .addToBackStack(null) // To allow to come back to the previous fragment when back is pressed
                    .commit();
        });
    }

    private void fillViewsDelete(View view, boolean deletable) {
        Button deleteButton = view.findViewById(R.id.fbi_delete_button);
        deleteButton.setVisibility(deletable ? View.VISIBLE : View.GONE);
        deleteButton.setOnClickListener(v -> this.openDialog(DialogID.DIALOG_DELETE, true));
    }

    private void setOnProfileLoadedListener() {
        assert getArguments() != null;
        boolean showOwner = getArguments().getBoolean(BOOK_SHOW_OWNER_KEY);
        if (owner != null || !showOwner)
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
                        if (data != null) {
                            owner = new UserProfile(book.getOwnerID(), data, getResources());
                            assert getView() != null;
                            fillViewsOwner(getView(), true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        unsetOnProfileLoadedListener();
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

    private void deleteBook() {
        book.deleteFromAlgolia((json, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
                return;
            }

            book.deleteFromFirebase(UserProfile.localInstance);
            assert getActivity() != null;
            getActivity().onBackPressed();
        });
    }

    @Override
    protected void openDialog(@NonNull BookInfoFragment.DialogID dialogId, boolean dialogPersist) {
        super.openDialog(dialogId, dialogPersist);

        Dialog dialogInstance = null;
        switch (dialogId) {
            case DIALOG_DELETE:
                dialogInstance = new AlertDialog.Builder(getContext())
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteBook())
                        .setNegativeButton(android.R.string.no, null)
                        .show();
        }

        if (dialogInstance != null) {
            setDialogInstance(dialogInstance);
        }

    }

    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        popupImage = root.findViewById(R.id.pvi_image);
    }

    @Override
    public void onPopupShow(@Nullable String popupTag) {
        GlideRequest<Drawable> thumbnail = GlideApp
                .with(this)
                .load(book.getBookThumbnailReferenceOrNull())
                .fitCenter();

        assert getContext() != null;
        GlideApp.with(getContext())
                .load(book.getBookPictureReferenceOrNull())
                .thumbnail(thumbnail)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fitCenter()
                .into(popupImage);
    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {

    }

    public enum DialogID {
        DIALOG_DELETE,
    }
}
