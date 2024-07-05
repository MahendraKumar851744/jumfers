package com.jumfers.mocktestseries.utils;

import android.os.AsyncTask;

import com.jumfers.mocktestseries.listeners.DatabaseOperations;
import com.jumfers.mocktestseries.listeners.InsertionCallback;


public class InsertAsyncTask<T> extends AsyncTask<Void, Void, Void> {
    private T data;
    private DatabaseOperations<T> databaseOperations;
    private InsertionCallback callback;
    public InsertAsyncTask(T data, DatabaseOperations<T> databaseOperations) {
        this.data = data;
        this.databaseOperations = databaseOperations;
    }
    public InsertAsyncTask(T data, DatabaseOperations<T> databaseOperations, InsertionCallback callback) {
        this.data = data;
        this.databaseOperations = databaseOperations;
        this.callback = callback;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        databaseOperations.insert(data);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            callback.onInsertionComplete();
        }
    }

}
