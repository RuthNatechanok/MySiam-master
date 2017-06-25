package appewtc.masterung.mysiam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private EditText userEditText, passwordEditText;
    private TextView textView;
    private Button button;
    private String userString, passwordString;
    private MyAlert myAlert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial View
        initialView();

        //TextView Controller
        textViewController();

        //Button Controller
        buttonController();


    }   // Mathod Main

    private void buttonController() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Value From Edit Text
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString();

                //Check Space
                if (userString.length() == 0 || passwordString.length() == 0) {
                    //Have Space
                    myAlert.myDialog(getResources().getString(R.string.titleHaveSpace),
                            getResources().getString(R.string.messageHaveSpace));

                } else {
                    //No Space
                    checkUserAndPass();
                }

            }
        });
    }

    private void checkUserAndPass() {

        try {

            String urlPHP = "http://androidthai.in.th/siam/getAllDataMaster.php";
            GetAllData getAllData = new GetAllData(MainActivity.this);
            getAllData.execute(urlPHP);
            String strJSON = getAllData.get();
            Log.d("SiamV1", "JSON ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            boolean b = true;
            String[] strings = new String[]{"id", "Name",
                    "User", "Password"};
            String[] loginStrings1 = new String[strings.length];


            for (int i=0; i<jsonArray.length(); i+=1) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (userString.equals(jsonObject.getString("User"))) {


                    b = false;

                    for (int i1=0; i1<strings.length; i1++) {
                        loginStrings1[i1] = jsonObject.getString(strings[i1]);
                        Log.d("SiamV1", "loginString[" + i1 + "] ==> " + loginStrings1[i1]);
                    }

                }

            }   // for

            if (b) {
                myAlert.myDialog(getResources().getString(R.string.titleUserFalse),
                        getResources().getString(R.string.messageUerFalse));
            } else if (passwordString.equals(loginStrings1[3])) {
                Toast.makeText(MainActivity.this, "Welcome " + loginStrings1[1],
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("Login", loginStrings1);
                startActivity(intent);
                finish();
            } else {
                myAlert.myDialog(getResources().getString(R.string.titlePasswordFalse),
                        getResources().getString(R.string.messagePasswordFalse));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void textViewController() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent to NewRegisterActivity
                Intent intent = new Intent(MainActivity.this, NewRegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initialView() {

        myAlert = new MyAlert(MainActivity.this);

        userEditText = (EditText) findViewById(R.id.edtUser);
        passwordEditText = (EditText) findViewById(R.id.edtPassword);
        textView = (TextView) findViewById(R.id.txtNewRegister);
        button = (Button) findViewById(R.id.btnLogin);

    }

}   // Main Class นี่คือ คลาสหลัก
