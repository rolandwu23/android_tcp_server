package com.eonreality.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnTCPMessageRecievedListener {

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TCPCommunicator writer =TCPCommunicator.getInstance();
        TCPCommunicator.addListener(this);
        writer.init(1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void someButtonClicked(View view)
    {
        JSONObject obj = new JSONObject();
        try
        {
            if(view.getId()==R.id.btnSendToClient)
            {
                obj.put(EnumsAndStatics.MESSAGE_TYPE_FOR_JSON, EnumsAndStatics.MessageTypes.MessageFromServer);
                EditText txtContent = (EditText)findViewById(R.id.txtContentToSend);
                obj.put(EnumsAndStatics.MESSAGE_CONTENT_FOR_JSON, txtContent.getText().toString());
            }

            final JSONObject jsonReadyForSend=obj;
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    TCPCommunicator.writeToSocket(jsonReadyForSend);
                }
            });
            thread.start();

        }
        catch(Exception e)
        {

        }

    }

    @Override
    public void onTCPMessageRecieved(String message) {
        // TODO Auto-generated method stub
        final String theMessage=message;
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try
                {
                    EditText editTxt = (EditText)findViewById(R.id.txtInputFromClient);
                    editTxt.setText(theMessage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }
}
