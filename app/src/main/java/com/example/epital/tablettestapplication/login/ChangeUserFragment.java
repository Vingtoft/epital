package com.example.epital.tablettestapplication.login;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.epital.tablettestapplication.ApplicationObject;
import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.connection.ServerLogIn;
import com.example.epital.tablettestapplication.database.LogInDatabaseHandler;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by oscarandersen on 30/09/14.
 */

public class ChangeUserFragment extends Fragment implements View.OnClickListener {

    EditText username, password;
    TextView errorMessage, header;
    Button login;
    ServerLogIn getSecurityTokenFromServer;
    LogInDatabaseHandler logInDatabaseHandler;
    LoginFragmentCommunication comm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_change_user_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        init();
    }

    private void init() {
        comm = (LoginFragmentCommunication) getActivity();
        login = (Button) getActivity().findViewById(R.id.loginButton);
        login.setOnClickListener(this);
        logInDatabaseHandler = new LogInDatabaseHandler(getActivity());
        errorMessage = (TextView) getActivity().findViewById(R.id.loginErrorMessage);
        header = (TextView) getActivity().findViewById(R.id.createUserText);
        getSecurityTokenFromServer = new ServerLogIn(okHttpCallback);
        username = (EditText) getActivity().findViewById(R.id.createUserUsername);
        password = (EditText) getActivity().findViewById(R.id.createUserPassword);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                stateMachine(1);
                return true;
            }
        });
    }

    private void stateMachine(int state) {
        switch (state) {
            case 1:
                // Try to login when the user hits "Done" on keyboard or "Log ind" button
                logIn();
                break;
            case 2:
                // User has entered a wrong username. Apply GUI
                loginFail();
                break;
            case 3:
                //User has been logged in, and the security token has been saved in the DB.
                comm.stateMachine(3);
                break;
        }
    }

    private boolean logIn() {

        String brugernavn = username.getText().toString();
        String kodeord = password.getText().toString();
        getSecurityTokenFromServer.getSecurityToken(brugernavn, kodeord);
        return true;
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            if (inputMessage.what == 1) {
                //Login success, save the input in Database
                logInDatabaseHandler.saveToken(inputMessage.obj.toString());
                //set token in application object

                stateMachine(3);

            } else if (inputMessage.what == 2) {
                stateMachine(2);
            }
        }
    };


    Callback okHttpCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            System.out.println("Failure in ChangeUserFragment okHttpCallBack!");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Gson gson = new Gson();
            if (response.code() == 200) {
                //Login success, extract the security token
                Token token = gson.fromJson(response.body().string(), Token.class);
                //Send security token and status indicator to main thread
                Message message = mHandler.obtainMessage(1, token.getToken());
                message.sendToTarget();

            } else {
                System.out.println("Svar fra server: " + response.body().string());
                //Send status indicator to main thread
                Message message = mHandler.obtainMessage(2);
                message.sendToTarget();
            }
        }
    };

    private class Token {
        String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @Override
    public void onClick(View button) {
        if (button == login) {
            logIn();
        }
    }


    /**
     * *********************
     * __   GUI methods   __
     * *********************
     */

    public void loginFail() {
        //set error message
        errorMessage.setText("Fejl i CPR-nummer eller kodeord");
        //remove password text
        password.setText("");
        //move to
    }


}












