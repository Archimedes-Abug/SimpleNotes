package com.otherdevs.notes.simplenotes;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button newButton,saveButton,openButton;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newButton=(Button)findViewById(R.id.newButton);
        saveButton=(Button)findViewById(R.id.saveButton);
        openButton=(Button)findViewById(R.id.openButton);
        text=(EditText)findViewById(R.id.text);
    }

    private void readItems(String fileName) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, fileName+".txt");
        try {
//            items = new ArrayList<String>(FileUtils.readLines(todoFile));
            text.setText(FileUtils.readFileToString(todoFile));
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG);
        }
    }

    private void writeItems(String fileName) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, fileName+".txt");
        try {
//            FileUtils.writeLines(todoFile, text.getText().toString());
            FileUtils.writeByteArrayToFile(todoFile, text.getText().toString().getBytes());
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG);
        }
    }

    private void writeToFile(String fileName, String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String filename, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename + ".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    public void buttonAction(View v) {
        final EditText fileName=new EditText(this);
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setView(fileName);

        if (v.getId() == R.id.saveButton) {
            ad.setMessage("Save File");

            ad.setPositiveButton("Save",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
//                        FileOutputStream fout=openFileOutput(fileName.getText().toString()+".txt",MODE_WORLD_READABLE);
//                        fout.write(text.getText().toString().getBytes());
//                        writeItems(fileName.getText().toString());
                        writeToFile(fileName.getText().toString(), text.getText().toString(), MainActivity.this);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Occured: "+e, Toast.LENGTH_LONG).show();
                    }
                }
            });

            ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            ad.show();

        }

        if(v.getId()==R.id.openButton) {
            ad.setMessage("Open File");

            ad.setPositiveButton("Open",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

//                    int c;
//                    text.setText("");
//
//                    try {
//                        FileInputStream fin = openFileInput(fileName.getText().toString()+".txt");
//
//                        while ((c = fin.read()) != -1)
//                        {
//                            text.setText((text.getText().toString() + Character.toString((char) c)));
//                        }
                    try{
//                        readItems(fileName.getText().toString()+".txt");
                        text.setText(readFromFile(fileName.getText().toString(), MainActivity.this));
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Occured: "+e,Toast.LENGTH_LONG).show();
                    }
                }
            });

            ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            ad.show();
        }

        if(v.getId()==R.id.newButton) {
            text.setText("");
        }
    }
}

