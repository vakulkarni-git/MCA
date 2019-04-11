package com.git.mca;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;

public class AsyncTaskActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynctask);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        new DownloadImageAsyncTask(imageView)
                .execute("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTKSQcz1ZxXf-XGqsgEkR5MOXUeZwyUxxTPoGb6cLhL-4YcCKoCA");

        requestQueue = Volley.newRequestQueue(this);
        makeVolleyRequest();
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageAsyncTask(ImageView imageView) {
            Log.d("DownloadImageAsyncTask", "Constructor");
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("DownloadImageAsyncTask", "onPreExecute");
            imageView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Log.d("DownloadImageAsyncTask", "doInBackground");
            Log.d("DownloadImageAsyncTask", "url : " + urls[0]);
            String url = urls[0];
            Bitmap imageBmp = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                imageBmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("DownloadImageAsyncTask", e.getMessage());
                e.printStackTrace();
            }
            return imageBmp;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d("DownloadImageAsyncTask", "onPostExecute");
            imageView.setImageBitmap(result);
        }
    }

    void makeVolleyRequest() {
        String url = "https://payment.cloud.altbalaji.com/products";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", "Response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Error: " + error.toString());
                    }
                });
        requestQueue.add(request);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}
