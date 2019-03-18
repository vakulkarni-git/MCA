package com.git.mca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    CharSequence[] items = {"Android", "iOS", "Windows"};
    boolean[] itemsChecked = new boolean [items.length];

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
                startActivity(new Intent("com.git.mca.activity2"));
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("This is a sample dialog...")
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
}
