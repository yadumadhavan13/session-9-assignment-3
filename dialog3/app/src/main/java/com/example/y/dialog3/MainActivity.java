package com.example.y.dialog3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnStart;
    TextView textView;
    EditText editText;
    MyAsyncTask myAsyncTask;
    String enteredSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_textview);

        btnStart=(Button)findViewById(R.id.start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });


    }

    class MyAsyncTask extends AsyncTask<String, Integer, Void> {

        boolean running;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(String... params) {
            String param1;
            param1=params[0];

            int i = i = Integer.parseInt(param1);
            int timeInMisec = i*100;
            while(running){
                try {
                    Thread.sleep(timeInMisec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i-- == 0){
                    running = false;
                }

                publishProgress(i);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(String.valueOf(values[0]));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;

            progressDialog = ProgressDialog.show(MainActivity.this, "", "Please Wait!");

            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    running = false;
                }
            });

            Toast.makeText(MainActivity.this, "Progress Start", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Progress Ended", Toast.LENGTH_LONG).show();
            textView.setText("After AsynTask");
            progressDialog.dismiss();
            addSucessDiag();
        }

    }

    public void addItem(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialog.setView(inflater.inflate(R.layout.custom_dialog, null))
                // Add action buttons

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog diag = (Dialog) dialog;
                        editText = (EditText) diag.findViewById(R.id.et_entered_sec);
                        enteredSecond = editText.getText().toString();

                        myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute(enteredSecond);

                        Log.e("addItem ",enteredSecond);

                    }
                });
        alertDialog.show();
    }

    public void addSucessDiag(){
        TextView title = new TextView(this);
        title.setText("Successful");
        title.setPadding(10, 10, 10, 10);
        title.setTextColor(Color.parseColor("#76caec"));
        title.setTextSize(23);

        TextView message = new TextView(this);
        message.setText("Completed");
        message.setPadding(10, 10, 10, 10);
        message.setTextSize(18);



        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.e("addSucessDiag ","Success");
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);
        builder.setView(message);
        builder.setCancelable(true);
        builder.setNeutralButton("OK", onClick);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
