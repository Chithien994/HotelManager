package com.tcn.hotelmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcn.database.FeedReaderContract;
import com.tcn.database.FeedReaderDbHelper;
import com.tcn.handle.Handle;
import com.tcn.handle.MyAction;
import com.tcn.handle.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class LoginActivity extends AppCompatActivity {
    private Calendar today = Calendar.getInstance();
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    private EditText txtEmailLogIn;
    private EditText txtPassWordLogIn;
    private TextView txtForgotPassword;
    private Button btnLogIn;
    private FeedReaderDbHelper dbHelper;
    private static String LOGIN_ACTIVITY = "LOGIN_ACTIVITY";
    private boolean df = true;
    private String namePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        namePhone = android.os.Build.MODEL;
        MyAction.setNameDevice(this, namePhone+"-"+sdfDate.format(today.getTime()));

        addControls();
        addEvents();
    }

    private void addEvents() {
        if (df){
            Log.d(LOGIN_ACTIVITY,"Đã khởi tạo tài khoản mặt định (admin | admin)!");
            Toast.makeText(this, "Đã khởi tạo tài khoản mặt định (admin | admin)!", Toast.LENGTH_LONG).show();
        }else {
            Log.e(LOGIN_ACTIVITY,"Không cần tạo thêm tài khoản mặt định!");
        }
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Đang cập nhật!", Toast.LENGTH_LONG).show();
            }
        });

        //Bug login
        findViewById(R.id.layoutBug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoomActivity();
                Toast.makeText(LoginActivity.this, "Bug login!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkLogin() {

        if (txtEmailLogIn.getText().toString().equals("") || txtEmailLogIn.getText().toString().equals(" ")){
            Toast.makeText(this, "Bạn chưa nhập tài khoản!", Toast.LENGTH_LONG).show();
        }else if (txtPassWordLogIn.getText().toString().equals("") || txtPassWordLogIn.getText().toString().equals(" ")){
            Toast.makeText(this, "Bạn chưa nhập mật khẩu!",Toast.LENGTH_LONG).show();
        }else {
            try {
                Cursor cf = dbHelper.getData("SELECT " +FeedReaderContract.Users.COLUMN_ID+
                        " FROM "+ FeedReaderContract.Users.TABLE_NAME+
                " WHERE "+FeedReaderContract.Users.COLUMN_USER+
                " = '"+txtEmailLogIn.getText().toString().toLowerCase()+
                "' AND "+FeedReaderContract.Users.COLUMN_PASS+
                " = '"+txtPassWordLogIn.getText().toString()+"'");
                if (cf.getCount()>0){
                    openRoomActivity();
                }else {
                    Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác!",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác!",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void openRoomActivity(){
        Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
        startActivity(intent);
        finish();
    }

    private void addControls() {
        dbHelper = new FeedReaderDbHelper(this,
                FeedReaderDbHelper.DATABASE_NAME,
                null,
                FeedReaderDbHelper.DATABASE_VERSION);
        dbHelper.queryData(FeedReaderDbHelper.SQL_CREATE_TABLE_ROOM);
        dbHelper.queryData(FeedReaderDbHelper.SQL_CREATE_TABLE_CUSTOMER);
        dbHelper.queryData(FeedReaderDbHelper.SQL_CREATE_TABLE_USERS);
        df = dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Users.TABLE_NAME +
                " VALUES(1, 'admin', 'admin','0')");

        txtEmailLogIn = findViewById(R.id.txtEmailLogIn);
        txtPassWordLogIn = findViewById(R.id.txtPassWordLogIn);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAction.setAction(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyAction.setAction(this, false);
    }
}
