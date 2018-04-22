package it.polito.mad.mad2018.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class IsbnQuery extends AsyncTask<String, Object, Volumes> {

    private final WeakReference<Context> context;
    private final WeakReference<TaskListener> listener;

    public IsbnQuery(@NonNull Context context, @NonNull TaskListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = new WeakReference<>(listener);
    }

    private static boolean isNetworkConnected(@NonNull Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected Volumes doInBackground(String... isbns) {

        if (isCancelled())
            return null;

        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        final Books booksClient = new Books.Builder(
                new ApacheHttpTransport(), jsonFactory, null).build();

        List volumesList;
        Volumes volumes;
        try {
            volumesList = booksClient.volumes().list("isbn:" + isbns[0]);
            // Execute the query.
            volumes = volumesList.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return volumes;

    }

    @Override
    protected void onPreExecute() {

        Context context = this.context.get();
        TaskListener listener = this.listener.get();
        if (context != null && listener != null) {
            if (!isNetworkConnected(context)) {
                cancel(true);
                listener.onNoNetworkConnection();
            } else {
                listener.onTaskStarted();
            }
        }
    }

    @Override
    protected void onPostExecute(Volumes volumes) {
        TaskListener listener = this.listener.get();
        if (listener != null) {
            listener.onTaskFinished(volumes);
        }
    }

    public interface TaskListener {
        void onTaskStarted();
        void onTaskFinished(Volumes result);

        void onNoNetworkConnection();
    }
}

