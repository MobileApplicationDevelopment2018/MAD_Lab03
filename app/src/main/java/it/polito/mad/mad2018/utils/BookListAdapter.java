package it.polito.mad.mad2018.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad.mad2018.R;
import it.polito.mad.mad2018.data.Book;

public class BookListAdapter extends RecyclerView.Adapter{
    private ArrayList<Book> bookList;

    public BookListAdapter(@NonNull List<Book> bookList){
        this.bookList = (ArrayList<Book>) bookList;
    }

    public BookListAdapter(@NonNull String[] bookNames){
        // TODO: queries for retrieving book information
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.book_search_item, parent, false);
        
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener{
        private TextView mBookTitle, mBookAuthor;
        private ImageView mBookPicture;

        public ListViewHolder(@NonNull View view){
            super(view);
            mBookTitle = view.findViewById(R.id.fbs_book_item_title);
            mBookAuthor = view.findViewById(R.id.fbs_book_item_author);
            mBookPicture = view.findViewById(R.id.fbs_book_item_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @SuppressLint("NewApi")
        public void bindView(int position){
            // TODO: change these values to the correct ones
            mBookTitle.setText(bookList.get(position).getTitle());
            mBookAuthor.setText(String.join(",", bookList.get(position).getAuthors()));
            mBookPicture.setImageResource(R.drawable.default_header);
        }
    }
}
