package edu.pitt.bcy3.scriptfathom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (MyView)findViewById(R.id.drawable);
        if(myView == null){
            Log.d("ScriptFathom","myView is null");
        }

        myView.setMA(this);

        ImageButton sendButton = (ImageButton)findViewById(R.id.btn_settings);
        sendButton.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY); //set pen color to black

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
          //  return true;
        //}
        return super.onOptionsItemSelected(item);
    }



    public void clearClicked(View view){
        myView.clear();
    }

    public void setTrashNormal(){
        ImageButton trashbutton = (ImageButton)findViewById(R.id.btn_clear);
        trashbutton.setBackgroundColor(getResources().getColor(R.color.trash_normal));
    }

    public void sendClicked(View view){
        myView.send();
    }

    public void setResponse(String response){

        TextView responseView = (TextView)findViewById(R.id.response_field);
        if (response != null){
            if(response.compareTo("null") == 0){
                responseView.setText("[  ]");
            } else {
                responseView.setText("[ Recognized as: "+response+" ]");
            }
        }

        unsetSending();

    }

    public void unsetSending(){
        ImageButton sendButton = (ImageButton)findViewById(R.id.btn_send);
        sendButton.setBackgroundColor(getResources().getColor(R.color.ready));

        ImageButton trashbutton = (ImageButton)findViewById(R.id.btn_clear);
        trashbutton.setBackgroundColor(getResources().getColor(R.color.trash_ready));

    }

    public void setSending(){
        ImageButton sendButton = (ImageButton)findViewById(R.id.btn_send);
        sendButton.setBackgroundColor(getResources().getColor(R.color.not_ready));
    }

    public void settingsClicked(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        myView.clear();

    }

    public void setButtonColor(int c) {
        ImageButton sendButton = (ImageButton)findViewById(R.id.btn_settings);
        //sendButton.setBackgroundColor(c);
        sendButton.setColorFilter(c, PorterDuff.Mode.MULTIPLY);
    }

}
