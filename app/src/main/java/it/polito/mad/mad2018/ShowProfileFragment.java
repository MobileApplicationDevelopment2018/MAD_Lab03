package it.polito.mad.mad2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.Locale;

import it.polito.mad.mad2018.data.UserProfile;
import it.polito.mad.mad2018.utils.GlideApp;
import it.polito.mad.mad2018.utils.GlideRequest;
import it.polito.mad.mad2018.utils.Utilities;

public class ShowProfileFragment extends Fragment {

    private static final String EDITABLE_KEY = "editable_key";

    private OnShowOwnedBooksClickListener onShowOwnedBooksClickListener;
    private boolean isEditable;

    public ShowProfileFragment() { /* Required empty public constructor */ }

    public static ShowProfileFragment newInstance(@NonNull UserProfile profile, boolean isEditable) {
        ShowProfileFragment fragment = new ShowProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(UserProfile.PROFILE_INFO_KEY, profile);
        args.putSerializable(EDITABLE_KEY, isEditable);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_show_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getActivity() != null;
        assert getView() != null;
        assert getArguments() != null;

        final UserProfile profile = (UserProfile) getArguments().getSerializable(UserProfile.PROFILE_INFO_KEY);
        isEditable = getArguments().getBoolean(EDITABLE_KEY);
        assert profile != null;

        getActivity().setTitle(profile.isLocal()
                ? getString(R.string.my_profile)
                : getString(R.string.user_profile, profile.getUsername()));

        // Fill the views with the data
        fillViews(profile);

        // MailTo button
        final ImageButton mailToButton = getView().findViewById(R.id.sp_mail_icon);
        mailToButton.setVisibility(profile.isLocal() ? View.GONE : View.VISIBLE);
        mailToButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("mailto:" + profile.getEmail());
            Intent mailTo = new Intent(Intent.ACTION_SENDTO, uri);
            if (mailTo.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mailTo);
            }
        });

        // ShowCity button
        final ImageButton showCityButton = getView().findViewById(R.id.sp_locate_icon);
        showCityButton.setEnabled(profile.getLocation() != null);
        showCityButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://maps.google.co.in/maps?q=" + profile.getLocation());
            Intent showCity = new Intent(Intent.ACTION_VIEW, uri);
            if (showCity.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(showCity);
            }
        });

        // ShowBooks button
        final ImageButton showBooksButton = getView().findViewById(R.id.sp_show_books);
        showBooksButton.setVisibility((profile.isLocal() || onShowOwnedBooksClickListener == null)
                ? View.INVISIBLE : View.VISIBLE);
        showBooksButton.setOnClickListener(v -> onShowOwnedBooksClickListener.OnShowOwnedBooksClick(profile));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (isEditable) {
            inflater.inflate(R.menu.menu_show_profile, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof ShowProfileFragment.OnShowOwnedBooksClickListener) {
            this.onShowOwnedBooksClickListener = (ShowProfileFragment.OnShowOwnedBooksClickListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.onShowOwnedBooksClickListener = null;
    }

    private void fillViews(@NonNull UserProfile profile) {
        assert getView() != null;

        TextView username = getView().findViewById(R.id.sp_username);
        TextView location = getView().findViewById(R.id.sp_location);
        TextView biography = getView().findViewById(R.id.sp_description);
        ImageView imageView = getView().findViewById(R.id.sp_profile_picture);

        RatingBar rating = getView().findViewById(R.id.sp_rating_bar);
        TextView ownedBooks = getView().findViewById(R.id.sp_owned_books_number);
        TextView lentBooks = getView().findViewById(R.id.sp_lent_books_number);
        TextView borrowedBooks = getView().findViewById(R.id.sp_borrowed_books_number);
        TextView toBeReturnedBooks = getView().findViewById(R.id.sp_to_be_returned_number);

        username.setText(profile.getUsername());
        location.setText(profile.getLocationOrDefault());

        getView().findViewById(R.id.sp_card_description).setVisibility(
                Utilities.isNullOrWhitespace(profile.getBiography())
                        ? View.GONE : View.VISIBLE
        );
        biography.setText(profile.getBiography());

        GlideRequest<Drawable> thumbnail = GlideApp
                .with(this)
                .load(profile.getProfilePictureThumbnail())
                .centerCrop();

        GlideApp.with(this)
                .load(profile.getProfilePictureReference())
                .signature(new ObjectKey(profile.getProfilePictureLastModified()))
                .thumbnail(thumbnail)
                .fallback(R.drawable.default_header)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(imageView);

        rating.setRating(profile.getRating());

        Locale currentLocale = getResources().getConfiguration().locale;
        ownedBooks.setText(String.format(currentLocale, "%d", profile.getOwnedBooksCount()));
        lentBooks.setText(String.format(currentLocale, "%d", profile.getLentBooksCount()));
        borrowedBooks.setText(String.format(currentLocale, "%d", profile.getBorrowedBooksCount()));
        toBeReturnedBooks.setText(String.format(currentLocale, "%d", profile.getToBeReturnedBooksCount()));
    }

    public interface OnShowOwnedBooksClickListener {
        void OnShowOwnedBooksClick(UserProfile profile);
    }
}
