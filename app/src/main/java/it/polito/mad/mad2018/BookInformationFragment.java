package it.polito.mad.mad2018;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import it.polito.mad.mad2018.utils.FragmentDialog;

public class BookInformationFragment extends FragmentDialog<BookInformationFragment.DialogID> {

    public static AddBookFragment newInstance() {
        AddBookFragment fragment = new AddBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void openDialog(@NonNull BookInformationFragment.DialogID dialogId, boolean dialogPersist) {
        super.openDialog(dialogId, dialogPersist);

        Dialog dialogInstance = null;
        switch (dialogId) {

        }

        if (dialogInstance != null) {
            setDialogInstance(dialogInstance);
        }

    }

    public enum DialogID {
        DIALOG_LOADING,
        DIALOG_SAVING,
        DIALOG_ADD_PICTURE,
    }
}
