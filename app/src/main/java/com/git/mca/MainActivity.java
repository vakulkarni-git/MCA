package com.git.mca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String preferencesName = "SETTINGS";

    int requestCode = 1;

    CharSequence[] items = {"SMS", "Email", "Notifications"};
    boolean[] itemsChecked = new boolean [items.length];

    private String CHANNEL_ID = "com.git.mca";
    private int notificationID = 1;

    private ProgressDialog progressDialog;
    private int progress = 0;
    private Handler progressHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (progress > 100) {
                progressDialog.dismiss();
            } else {
                progress++;
                progressDialog.incrementProgressBy(1);
                progressHandler.sendEmptyMessageDelayed(0, 100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        preferences = this.getSharedPreferences(preferencesName, MODE_PRIVATE);

        TextView label = findViewById(R.id.label);

        String chosenDate = preferences.getString(PickerActivity.CHOSEN_DATE, "NO_DATE");
        String chosenTime = preferences.getString(PickerActivity.CHOSEN_TIME, "NO_TIME");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button dialogButton = findViewById(R.id.dialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        Button progressDialogButton = findViewById(R.id.progressButton);
        progressDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
                progress = 0;
                progressDialog.setProgress(progress);
                progressHandler.sendEmptyMessage(progress);
            }
        });

        Button activityButton = findViewById(R.id.activityButton);
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.git.mca.activity2");
//                Bundle extras = new Bundle();
//                extras.putString("NameHint", "Any text you want here");
//                intent.putExtras(extras);
//                startActivityForResult(intent, requestCode);
                Intent intent = new Intent(getBaseContext(), ListAndSpinnerSample.class);
                startActivity(intent);
            }
        });

        String phoneNumber = "134234234";
        String message = "abcd";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", "5556");
        intent.putExtra("sms_body", "Test SMS message!");
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        createNotificationChannel();
        Button notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification();
            }
        });

        Button sendEmailButton = findViewById(R.id.sendEmailButton);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] to = {"vakulkarni@git.edu"};
                String [] cc = null;

                sendEmail(to, cc, "Hello", "Hello there!");
            }
        });
    }

    public class SMSReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length];

                for (int i=0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
            }
        }
    }

    private void sendEmail(String [] to, String [] cc, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        try {
            InputStream in = OpenHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException ex) {}

        return bitmap;
    }

    private JSONObject callWebService(String url) {
        JSONObject jsonObject = null;
        try {
            InputStream in = OpenHttpConnection(url);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            jsonObject = new JSONObject(responseStrBuilder.toString());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }

        return in;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("I agree to receive...")
                        .setPositiveButton("OK", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getBaseContext(),
                                                "OK Clicked!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .setNegativeButton("Cancel", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getBaseContext(),
                                                "Cancel Clicked!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .setMultiChoiceItems(items, itemsChecked, new
                                DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        Toast.makeText(getBaseContext(),
                                                items[which] + (isChecked ? " checked!" : " unchecked!"),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .create();
            case 1:
                progressDialog = new ProgressDialog(this);
                progressDialog.setIcon(android.R.drawable.ic_dialog_info);
                progressDialog.setTitle("Downloading files...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Hide", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(),
                                        "Hide clicked!", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(),
                                        "Cancel clicked!", Toast.LENGTH_SHORT).show();
                            }
                        });
                return progressDialog;
        }

        return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, data.getData().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void displayNotification() {
        Intent intent = new Intent(this, NotificationView.class);
        intent.putExtra("notificationID", 1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
                .setContentTitle("My notification")
                .setContentText("Meeting starts in 5 minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(notificationID, builder.build());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
