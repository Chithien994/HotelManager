package com.tcn.hotelmanager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tcn.adapters.RoomAdapters;
import com.tcn.database.FeedReaderContract;
import com.tcn.database.FeedReaderDbHelper;
import com.tcn.handle.MyAction;
import com.tcn.handle.ServerAPI;
import com.tcn.models.RoomModels;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RoomActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgMenu;
    private Spinner spClolumnView;
    private RecyclerView rvRoom;
    private NavigationView navigationViewLeft;
    private LinearLayout layoutAll;
    private LinearLayout layoutDangO;
    private LinearLayout layoutCheckIn;
    private LinearLayout layoutCheckOut;
    private LinearLayout layoutEmpty;
    private LinearLayout layoutPhongSach;
    private LinearLayout layoutPhongBan;
    private LinearLayout layoutDangSua;

    private ArrayList<RoomModels> dsRoomModels;
    private RoomAdapters roomAdapters;
    private ArrayList<String> dsView;
    private int column = 3;
    private SharedPreferences pf;
    private SharedPreferences.Editor editor;
    private AlertDialog alertDialog;

    public FeedReaderDbHelper dbHelper;
    public ArrayList<String> dsType;
    public ArrayList<String> dsStatus;
    public ArrayList<String> dsPlan;
    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        view = getWindow().getDecorView().findViewById(android.R.id.content);
        JSONObject object = new JSONObject();
        try {
            object.put("name",MyAction.getNameDevice(this));
            object.put("token",MyAction.getToken(this));
            ServerAPI.post(RoomActivity.this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addControls();
        addEvents();
    }

    private void addEvents() {
        navigationViewLeft.setNavigationItemSelectedListener(mNavLeft);
        imgMenu.setOnClickListener(this);

        spClolumnView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ((TextView) spClolumnView.getSelectedView()).setTextColor(Color.WHITE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                spClolumnView.setGravity(Gravity.RIGHT);
                spClolumnView.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                if (i==0){
                    Log.d("ROOM_ACTIVITY","i: "+i);
                    spClolumnView.setSelection(1);
                    return;
                }
                if (column != i+2){
                    column = i+2;
                    editor.putInt("column",column);
                    editor.commit();
                    Log.d("ROOM_ACTIVITY","Save column view: "+pf.getInt("column",3));
                    showRoom(column);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        layoutDangO.setOnClickListener(this);
        layoutCheckIn.setOnClickListener(this);
        layoutCheckOut.setOnClickListener(this);
        layoutEmpty.setOnClickListener(this);
        layoutPhongSach.setOnClickListener(this);
        layoutPhongBan.setOnClickListener(this);
        layoutDangSua.setOnClickListener(this);

    }

    private void addControls() {
        pf = getSharedPreferences(getString(R.string.saveInfo), MODE_PRIVATE);
        editor = pf.edit();
        column = pf.getInt("column",3);
        Log.d("ROOM_ACTIVITY","column view: "+column);

        imgMenu = findViewById(R.id.imgMenu);
        spClolumnView = findViewById(R.id.spClolumnView);
        rvRoom = findViewById(R.id.rvRoom);
        navigationViewLeft = findViewById(R.id.navigationViewLeft);
        layoutDangO = findViewById(R.id.layoutDangO);
        layoutCheckIn = findViewById(R.id.layoutCheckIn);
        layoutCheckOut = findViewById(R.id.layoutCheckOut);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        layoutPhongSach = findViewById(R.id.layoutPhongSach);
        layoutPhongBan = findViewById(R.id.layoutPhongBan);
        layoutDangSua = findViewById(R.id.layoutDangSua);

        dbHelper = new FeedReaderDbHelper(this, FeedReaderDbHelper.DATABASE_NAME, null, FeedReaderDbHelper.DATABASE_VERSION);

        addListRoom();
        dsRoomModels = new ArrayList<>();

        addColumnView();
        getRoom();
        addListRoomType();
        addListRoomStatus();
        addListRoomPlan();
    }

    private void addListRoom() {
        if (pf.getBoolean("new",true)){
            dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                    " VALUES(1, '001', 0,0,0,'10:45 14/03/2018','10:45:23 16/03/2018','2.973.000','0','0',0)");
            dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                    " VALUES(2, '002', 1,0,0,'10:45 14/03/2018','10:45:23 16/03/2018','2.973.000','0','0',0)");
            dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                    " VALUES(3, '003', 2,0,0,'10:45 14/03/2018','23:45:23 15/03/2018','2.973.000','0','0',0)");
            dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                    " VALUES(4, '004', 3,0,0,'20:45 15/03/2018','10:45:23 16/03/2018','0','0','0',0)");
            dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                    " VALUES(5, '005', 0,0,1,'10:45 14/03/2018','10:45:23 16/03/2018','2.973.000','0','0',0)");
            dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                    " VALUES(6, '006', 1,0,2,'10:45 14/03/2018','10:45:23 16/03/2018','2.973.000','0','0',0)");
            editor.putBoolean("new",false);
            editor.commit();
        }

    }

    private void addListRoomPlan() {
        dsPlan = new ArrayList<>();
        dsPlan.add(0,"Phòng sạch");
        dsPlan.add(1,"Phòng bẩn");
        dsPlan.add(2,"Phòng đang sửa");
    }

    private void addListRoomStatus() {
        dsStatus = new ArrayList<>();
        dsStatus.add(0,"Phòng trống");
        dsStatus.add(1,"Chuẩn bị đến");
        dsStatus.add(2,"Đang ở");
        dsStatus.add(3,"Chuẩn bị đi");
    }

    private void addListRoomType() {
        dsType = new ArrayList<>();
        dsType.add(0,"VIP");
        dsType.add(1,"LOVE");
        dsType.add(2,"CO-BAN");
        dsType.add(3,"DORM");
    }

    private void addColumnView() {
        dsView = new ArrayList<>();
        dsView.add(0,"Số cột");
        dsView.add(1," 3 cột ");
        dsView.add(2," 4 cột ");
        dsView.add(3," 5 cột ");
        dsView.add(4," 6 cột ");
        dsView.add(5," 7 cột ");
        dsView.add(6," 8 cột ");
        dsView.add(7," 9 cột ");
        dsView.add(8," 10 cột ");
        spClolumnView.setAdapter(getAdapter(dsView));
        spClolumnView.setSelection(column-2);

    }

    public ArrayAdapter<String> getAdapter(ArrayList<String> arrayList){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }

    public void getRoom() {
        dsRoomModels.clear();
        Cursor cursor = dbHelper.getData("SELECT * FROM "+ FeedReaderContract.Room.TABLE_NAME+
                " ORDER BY "+FeedReaderContract.Room.COLUMN_NUMBER+" ASC");
        while (cursor.moveToNext()){
            dsRoomModels.add(new RoomModels(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getInt(10)));
        }

        showRoom(column);
    }

    public void getRoom(String sql){
        dsRoomModels.clear();
        Cursor cursor = dbHelper.getData(sql);
        while (cursor.moveToNext()){
            dsRoomModels.add(new RoomModels(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getInt(10)));
        }

        showRoom(column);
    }


    private void showRoom(int column) {
        rvRoom.setLayoutManager(new GridLayoutManager(this, column));
        roomAdapters = new RoomAdapters(this, dsRoomModels);
        rvRoom.setAdapter(roomAdapters);
        roomAdapters.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    NavigationView.OnNavigationItemSelectedListener mNavLeft = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.nav_dsdatphong:
                    break;
                case R.id.nav_dskhachhang:
                    break;
                case R.id.nav_huydatphong:
                    break;
                case R.id.nav_saochepdatphong:
                    break;
                case R.id.nav_sodophong:
                    break;
                case R.id.nav_thaydoidatphong:
                    break;
                default:break;
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            return true;
        }
    };



    public void handleAddRoom() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_room, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCloseDialog = dialogView.findViewById(R.id.imgCloseDialog);
        final TextView txtNumberDialog = dialogView.findViewById(R.id.txtNumberDialog);
        final Spinner spTypeDialog = dialogView.findViewById(R.id.spTypeDialog);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);
        final int[] type = {0};
        int soPhong = getSoPhong(dsRoomModels);
        String num = ""+soPhong;
        if (soPhong<10) num ="00"+soPhong;
        else    if (soPhong < 100) num = "0"+soPhong;
        txtNumberDialog.setText(num);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        spTypeDialog.setAdapter(getAdapter(dsType));

        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        spTypeDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type[0] = i;
                spTypeDialog.setGravity(Gravity.CENTER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Room.TABLE_NAME +
                        " VALUES(null, '"+txtNumberDialog.getText().toString()+"', "+ type[0]+",0,0,null,null,null,null,null,0)");
                alertDialog.cancel();
                getRoom();
            }
        });
    }

    private int getSoPhong(ArrayList<RoomModels> dsRoomModels) {
        for (int i = 1; i <= dsRoomModels.size(); i++){
            if (dsRoomModels.get(i-1).getId()!=i) return i;
        }
        return dsRoomModels.size()+1;
    }

    NavigationView.OnNavigationItemSelectedListener mNavRight = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutDangO:
                String sql = "SELECT * FROM "+ FeedReaderContract.Room.TABLE_NAME+
                        " ORDER BY "+FeedReaderContract.Room.COLUMN_NUMBER+" ASC";
                getRoom(sql);
                break;
            case R.id.layoutCheckIn:
                break;
            case R.id.layoutCheckOut:
                break;
            case R.id.layoutEmpty:
                break;
            case R.id.layoutPhongSach:
                break;
            case R.id.layoutPhongBan:
                break;
            case R.id.layoutDangSua:
                break;
            case R.id.imgMenu:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
            default:break;
        }
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
