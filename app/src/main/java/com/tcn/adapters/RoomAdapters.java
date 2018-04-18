package com.tcn.adapters;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcn.database.FeedReaderContract;
import com.tcn.handle.Handle;
import com.tcn.hotelmanager.R;
import com.tcn.hotelmanager.RoomActivity;
import com.tcn.models.RoomModels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by MyPC on 14/03/2018.
 */

public class RoomAdapters extends RecyclerView.Adapter<RoomAdapters.ViewHolder>{

    private RoomActivity roomActivity;
    private ArrayList<RoomModels> objects;
    private int resource;
    private int resourceAdd;
    private Calendar today = Calendar.getInstance();
    private Calendar tomorrow = Calendar.getInstance();
    private SimpleDateFormat sdfDate2 = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfHours = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
    private String str1 = "0";
    private String str2 = "0";
    public RoomAdapters(RoomActivity roomActivity, ArrayList<RoomModels> objects) {
        this.roomActivity = roomActivity;
        this.objects = objects;
        this.resource = R.layout.item_room;
        this.resourceAdd = R.layout.item_add;
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == objects.size()) ? resourceAdd : resource;
    }

    @Override
    public RoomAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view,resourceAdd);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position < objects.size()){
            final RoomModels roomModels = objects.get(position);
            holder.txtSoPhong.setText(roomModels.getRoomNumber());
            holder.txtLoaiPhong.setText(roomActivity.dsType.get(roomModels.getRoomType()));
            if (roomModels.getReserve()==RoomModels.DA_DAT){
                if (checkInDate(roomModels.getCheckInDate())){
                    holder.imgTrangThai.setImageResource(R.drawable.ic_check_in);
                }else {
                    if (checkOutDate(roomModels.getCheckOutDate())){
                        holder.imgTrangThai.setImageResource(R.drawable.ic_check_out);
                    }else{
                        holder.imgTrangThai.setImageResource(R.drawable.ic_people);
                    }
                }
            }else{
                holder.imgTrangThai.setImageResource(R.drawable.ic_check);
            }

            switch (roomModels.getRoomPlan()){
                case RoomModels.PHONG_SACH:
                    holder.layoutItem.setBackgroundResource(R.color.colorPhongSach);
                    break;
                case RoomModels.PHONG_BAN:
                    holder.layoutItem.setBackgroundResource(R.color.colorPhongBan);
                    break;
                case RoomModels.PHONG_DANG_SUA:
                    holder.layoutItem.setBackgroundResource(R.color.colorPhongDangSua);
                    break;
                default:break;
            }

            holder.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    optionRoom(roomModels);
                }
            });
            holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(roomActivity)
                            .setMessage("Bạn muốn xóa phòng "+roomModels.getRoomNumber()+"?")
                            .setPositiveButton("Không",null)
                            .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (roomModels.getReserve() == RoomModels.CHUA_DAT){
                                        roomActivity.dbHelper.queryData("DELETE FROM "+FeedReaderContract.Room.TABLE_NAME+
                                                " WHERE "+FeedReaderContract.Room.COLUMN_ID+" = "+roomModels.getId());
                                        roomActivity.getRoom();
                                    }else{
                                        Toast.makeText(roomActivity, "Chỉ xóa khi phòng trống!", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }).show();
                    return false;
                }
            });
        }else{
            holder.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    roomActivity.handleAddRoom();
                }
            });
        }

    }

    private boolean checkInDate(String dateTime) {

        String time = dateTime.split(" ")[0];
        String date = dateTime.split(" ")[1];
        String dayOld = date.split("/")[0];
        String monthOld = date.split("/")[1];
        String yearOld = date.split("/")[2];

        String dateFormatNow = sdfDate2.format(today.getTime())+" "+sdfTime.format(today.getTime());
        String dateFormatOld = yearOld+"/"+monthOld+"/"+dayOld+" "+time;
        Log.d("CHECK IN","dateFormatNow: "+dateFormatNow);
        Log.d("CHECK IN","dateFormatOld: "+dateFormatOld);

        return (dateFormatNow.compareTo(dateFormatOld) < 0)?true:false;

    }

    private boolean checkOutDate(String dateTime) {
        String date = dateTime.split(" ")[1];
        String dayOld = date.split("/")[0];
        String monthOld = date.split("/")[1];
        String yearOld = date.split("/")[2];
        String dateFormatNow = sdfDate2.format(today.getTime());
        String dateFormatOld = yearOld+"/"+monthOld+"/"+dayOld;
        Log.d("CHECK OUT","dateFormatNow: "+dateFormatNow);
        Log.d("CHECK OUT","dateFormatOld: "+dateFormatOld);
        return (dateFormatNow.compareTo(dateFormatOld) == 0)?true:false;

    }

    private void optionRoom(final RoomModels roomModels) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(roomActivity);
        LayoutInflater inflater = roomActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_option_room, null);
        dialogBuilder.setView(dialogView);

        TextView txtEstablish = dialogView.findViewById(R.id.txtEstablish);
        TextView txtCheckIn = dialogView.findViewById(R.id.txtCheckIn);
        TextView txtCheckOut = dialogView.findViewById(R.id.txtCheckOut);
        TextView txtPhong = dialogView.findViewById(R.id.txtPhong);

        txtPhong.setText("Phòng "+roomModels.getRoomNumber());

        if (roomModels.getReserve()==RoomModels.DA_DAT){
            txtCheckIn.setVisibility(View.GONE);
            txtCheckOut.setVisibility(View.VISIBLE);
        }else{
            txtCheckIn.setVisibility(View.VISIBLE);
            txtCheckOut.setVisibility(View.GONE);
        }

        if (roomModels.getRoomPlan() == RoomModels.PHONG_DANG_SUA){
            txtCheckOut.setVisibility(View.GONE);
            txtCheckIn.setVisibility(View.GONE);
        }

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        Display display = roomActivity.getWindowManager().getDefaultDisplay();

        int inWidth = display.getWidth();
        int inHeight = display.getHeight();
        int newWidth = (inHeight > inWidth) ? inWidth/2 : inHeight/2;
        int newHeight = (inHeight > inWidth) ?  inWidth/2 : inHeight/2;

        alertDialog.getWindow().setLayout(newWidth, newHeight); //Controlling width and height.

        txtEstablish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roomModels.getReserve() == RoomModels.DA_DAT){
                    handleEstablishService(roomModels);
                }else{
                    handleEstablish(roomModels);
                }
                alertDialog.cancel();
            }
        });

        txtCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCheckIn(roomModels);
                alertDialog.cancel();
            }
        });

        txtCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCheckOut(roomModels);
                alertDialog.cancel();
            }
        });
    }

    private void handleEstablish(final RoomModels roomModels) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(roomActivity);
        LayoutInflater inflater = roomActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_establish_room, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCloseDialog = dialogView.findViewById(R.id.imgCloseDialog);
        Button btnOK = dialogView.findViewById(R.id.btnOK);
        final TextView txtNumberDialog = dialogView.findViewById(R.id.txtNumberDialog);
        Spinner spTypeDialog = dialogView.findViewById(R.id.spTypeDialog);
        Spinner spPlanDialog = dialogView.findViewById(R.id.spPlanDialog);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        final int[] type = {roomModels.getRoomType()};
        final int[] plan = {roomModels.getRoomPlan()};

        txtNumberDialog.setText(roomModels.getRoomNumber());

        spTypeDialog.setAdapter(roomActivity.getAdapter(roomActivity.dsType));
        spPlanDialog.setAdapter(roomActivity.getAdapter(roomActivity.dsPlan));

        spTypeDialog.setSelection(type[0]);
        spPlanDialog.setSelection(plan[0]);

        spTypeDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spPlanDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                plan[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomActivity.dbHelper.queryData("UPDATE "+ FeedReaderContract.Room.TABLE_NAME+
                " SET "+FeedReaderContract.Room.COLUMN_NUMBER+" = '"+txtNumberDialog.getText().toString()+
                "', "+FeedReaderContract.Room.COLUMN_TYPE+" = "+type[0]+
                ", "+FeedReaderContract.Room.COLUMN_PLAN+" = "+plan[0]+
                " WHERE "+FeedReaderContract.Room.COLUMN_ID+" = "+roomModels.getId());
                roomActivity.getRoom();
                alertDialog.cancel();
            }
        });
    }

    private void handleCheckIn(final RoomModels roomModels) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(roomActivity);
        LayoutInflater inflater = roomActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_check_in_room, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCloseDialog = dialogView.findViewById(R.id.imgCloseDialog);
        Button btnOK = dialogView.findViewById(R.id.btnOK);
        Button btnChonNgayDen = dialogView.findViewById(R.id.btnChonNgayDen);
        Button btnChonNgayDi = dialogView.findViewById(R.id.btnChonNgayDi);
        final EditText txtNameDialog = dialogView.findViewById(R.id.txtNameDialog);
        final EditText txtCMTDialog = dialogView.findViewById(R.id.txtCMTDialog);
        final EditText txtPhoneDialog = dialogView.findViewById(R.id.txtPhoneDialog);
        final EditText txtSoNguoiDialog = dialogView.findViewById(R.id.txtSoNguoiDialog);
        final EditText txtGioDenDialog = dialogView.findViewById(R.id.txtGioDenDialog);
        final EditText txtPhutDenDialog = dialogView.findViewById(R.id.txtPhutDenDialog);
        final EditText txtGioDiDialog = dialogView.findViewById(R.id.txtGioDiDialog);
        final EditText txtPhutDiDialog = dialogView.findViewById(R.id.txtPhutDiDialog);
        final TextView txtNgayDenDialog = dialogView.findViewById(R.id.txtNgayDenDialog);
        final TextView txtNgayDiDialog = dialogView.findViewById(R.id.txtNgayDiDialog);
        final TextView txtPriceRoomDialog = dialogView.findViewById(R.id.txtPriceRoomDialog);
        TextView txtRoomNumberDialog = dialogView.findViewById(R.id.txtRoomNumberDialog);
        TextView txtRoomTypeDialog = dialogView.findViewById(R.id.txtRoomTypeDialog);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        txtRoomTypeDialog.setText(roomActivity.dsType.get(roomModels.getRoomType()));
        txtRoomNumberDialog.setText(roomModels.getRoomNumber());
        txtNgayDenDialog.setText(sdfDate.format(today.getTime()));
        txtNgayDiDialog.setText(sdfDate.format(tomorrow.getTime()));
        txtGioDenDialog.setText(sdfHours.format(today.getTime()));
        txtPhutDenDialog.setText(sdfMinute.format(today.getTime()));
        txtGioDiDialog.setText(sdfHours.format(today.getTime()));
        txtPhutDiDialog.setText(sdfMinute.format(today.getTime()));
        str1 = sdfDate2.format(today.getTime());
        str2 = sdfDate2.format(tomorrow.getTime());

        btnChonNgayDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str1 = "";
                handleSelectDate(txtNgayDenDialog);
            }
        });

        btnChonNgayDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str2 = "";
                handleSelectDate(txtNgayDiDialog);
            }
        });

        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gioDen = txtGioDenDialog.getText().toString()+":"+txtPhutDenDialog.getText().toString();
                String gioDi = txtGioDiDialog.getText().toString()+":"+txtPhutDiDialog.getText().toString();
                String name = txtNameDialog.getText().toString();
                String CMT = txtCMTDialog.getText().toString();
                String phone = txtPhoneDialog.getText().toString();
                int soNguoi = 1;
                try {
                    soNguoi = Integer.parseInt(txtSoNguoiDialog.getText().toString());
                }catch (Exception e){

                }
                String ngayDen = txtNgayDenDialog.getText().toString();
                String ngayDi = txtNgayDiDialog.getText().toString();

                if (str2.compareTo(str1)<0){
                    Toast.makeText(roomActivity , "Ngày đi không hợp lệ!", Toast.LENGTH_LONG).show();

                }else if (str2.compareTo(str1)==0
                        && txtGioDiDialog.getText().toString().compareTo(txtGioDenDialog.getText().toString())<=0){
                    Toast.makeText(roomActivity, "Cùng ngày thì giờ đi phải lớn hơn giờ đến!", Toast.LENGTH_LONG).show();

                }else {
                    roomActivity.dbHelper.queryData("UPDATE "+ FeedReaderContract.Room.TABLE_NAME+
                            " SET "+FeedReaderContract.Room.COLUMN_CHECK_IN_DATE+" = '"+gioDen+" "+ngayDen+
                            "', "+FeedReaderContract.Room.COLUMN_CHECK_OUT_DATE+" = '"+gioDi+" "+ngayDi+
                            "', "+FeedReaderContract.Room.COLUMN_TOTAL_PRICE+" = '0'"+
                            ", "+FeedReaderContract.Room.COLUMN_ROOM_CHARGE+" = '"+txtPriceRoomDialog.getText().toString()+
                            "', "+FeedReaderContract.Room.COLUMN_RESERVE+" = 1"+
                            ", "+FeedReaderContract.Room.COLUMN_NUM_PEOPLE+" = "+soNguoi+
                            " WHERE "+FeedReaderContract.Room.COLUMN_ID+" = "+roomModels.getId());
                    roomActivity.dbHelper.queryData("INSERT INTO "+ FeedReaderContract.Customer.TABLE_NAME +
                    " VALUES(null,'"+name+"','"+phone+"','"+CMT+"',"+roomModels.getId()+")");
                    roomActivity.getRoom();
                    alertDialog.cancel();
                }

            }
        });

        txtGioDenDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtGioDenDialog.getText().toString())>23){
                        txtGioDenDialog.setText("23");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPhutDenDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtPhutDenDialog.getText().toString())>59){
                        txtPhutDenDialog.setText("59");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtGioDiDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtGioDiDialog.getText().toString())>23){
                        txtGioDiDialog.setText("23");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPhutDiDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtPhutDiDialog.getText().toString())>59){
                        txtPhutDiDialog.setText("59");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void handleSelectDate(final TextView textView) {
        Handle.hideKeyboard(roomActivity, roomActivity.view);
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                today.set(Calendar.YEAR, i);
                today.set(Calendar.MONTH, i1);
                today.set(Calendar.DAY_OF_MONTH, i2);
                if (str1.equals("")) str1 = sdfDate2.format(today.getTime());
                if (str2.equals("")) str2 = sdfDate2.format(today.getTime());
                textView.setText(sdfDate.format(today.getTime()));
            }
        };
        new DatePickerDialog(
                roomActivity,
                callBack,
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void handleCheckOut(final RoomModels roomModels) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(roomActivity);
        LayoutInflater inflater = roomActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_check_out_room, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCloseDialog = dialogView.findViewById(R.id.imgCloseDialog);
        Button btnOK = dialogView.findViewById(R.id.btnOK);
        TextView txtNameDialog = dialogView.findViewById(R.id.txtNameDialog);
        TextView txtPhoneDialog = dialogView.findViewById(R.id.txtPhoneDialog);
        TextView txtCMTDialog = dialogView.findViewById(R.id.txtCMTDialog);
        TextView txtSoNguoiDialog = dialogView.findViewById(R.id.txtSoNguoiDialog);
        TextView txtRoomNumberDialog = dialogView.findViewById(R.id.txtRoomNumberDialog);
        TextView txtRoomTypeDialog = dialogView.findViewById(R.id.txtRoomTypeDialog);
        TextView txtNgayDenDialog = dialogView.findViewById(R.id.txtNgayDenDialog);
        TextView txtHienTaiDialog = dialogView.findViewById(R.id.txtHienTaiDialog);
        TextView txtNgayTraDialog = dialogView.findViewById(R.id.txtNgayTraDialog);
        TextView txtPriceDialog = dialogView.findViewById(R.id.txtPriceDialog);
        TextView txtSoNgayODialog = dialogView.findViewById(R.id.txtSoNgayODialog);
        TextView txtPriceRoomDialog = dialogView.findViewById(R.id.txtPriceRoomDialog);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        Cursor cursor = roomActivity.dbHelper.getData("SELECT * FROM "+FeedReaderContract.Customer.TABLE_NAME+
        " WHERE "+FeedReaderContract.Customer.COLUMN_ID_ROOM+" = "+roomModels.getId());
        String name="";
        String CMT="";
        String phone="";

        int i = cursor.getCount();
        while (cursor.moveToNext()){
            i--;
            String n = "";
            if (i<0) n ="\n";
            name += cursor.getString(1)+n;
            CMT += cursor.getString(3)+n;
            phone += cursor.getString(2)+n;

        }
        txtNameDialog.setText(name);
        txtPhoneDialog.setText(phone);
        txtCMTDialog.setText(CMT);

        txtNgayDenDialog.setText(roomModels.getCheckInDate());
        txtHienTaiDialog.setText(sdfTime.format(today.getTime())+" "+sdfDate.format(today.getTime()));
        txtNgayTraDialog.setText(roomModels.getCheckOutDate());
        txtRoomNumberDialog.setText(roomModels.getRoomNumber());
        txtRoomTypeDialog.setText(roomActivity.dsType.get(roomModels.getRoomType()));
        txtSoNguoiDialog.setText(roomModels.getNumberPeople()+"");

        int soNgay =  Handle.convertToInt(Handle.countTheDays(roomModels.getCheckInDate().split(" ")[1])+"");
        String ngayO ="";
        int numDay = 0;
        if (soNgay < 0){
            ngayO = "Chưa đến";
        }else if (soNgay == 0){
            ngayO = "Ngày đầu";
            numDay = 1;
        }else{
            ngayO = soNgay+" ngày";
            numDay = soNgay;
        }
        final int gia = Handle.convertToInt(roomModels.getTotalPrice()) + numDay*Handle.convertToInt(roomModels.getRoomCharge());

        txtPriceRoomDialog.setText(Handle.handleMoney(numDay*Handle.convertToInt(roomModels.getRoomCharge())));
        txtPriceDialog.setText(Handle.handleMoney(gia));
        txtSoNgayODialog.setText(ngayO);

        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(roomActivity)
                        .setTitle("Chú ý")
                        .setMessage("Thanh toán phòng "+roomModels.getRoomNumber()+" với tổng tiền "+Handle.handleMoney(gia)+"!")
                        .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                roomActivity.dbHelper.queryData("UPDATE "+ FeedReaderContract.Room.TABLE_NAME+
                                        " SET "+FeedReaderContract.Room.COLUMN_RESERVE+" = 0"+
                                        " WHERE "+FeedReaderContract.Room.COLUMN_ID+" = "+roomModels.getId());
                                roomActivity.dbHelper.queryData("DELETE FROM "+FeedReaderContract.Customer.TABLE_NAME+
                                        " WHERE "+FeedReaderContract.Customer.COLUMN_ID_ROOM+" = "+roomModels.getId());
                                roomActivity.getRoom();
                                Toast.makeText(roomActivity,"Thanh toán thành công!",Toast.LENGTH_LONG).show();
                                alertDialog.cancel();
                            }
                        })
                        .setPositiveButton("Không",null).show();

            }
        });
    }

    private void handleEstablishService(final RoomModels roomModels) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(roomActivity);
        LayoutInflater inflater = roomActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_establish_service_room, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCloseDialog = dialogView.findViewById(R.id.imgCloseDialog);
        Button btnOK = dialogView.findViewById(R.id.btnOK);
        Button btnChonNgayDen = dialogView.findViewById(R.id.btnChonNgayDen);
        Button btnChonNgayDi = dialogView.findViewById(R.id.btnChonNgayDi);
        final EditText txtNameDialog = dialogView.findViewById(R.id.txtNameDialog);
        final EditText txtCMTDialog = dialogView.findViewById(R.id.txtCMTDialog);
        final EditText txtPhoneDialog = dialogView.findViewById(R.id.txtPhoneDialog);
        final EditText txtSoNguoiDialog = dialogView.findViewById(R.id.txtSoNguoiDialog);
        final EditText txtGioDenDialog = dialogView.findViewById(R.id.txtGioDenDialog);
        final EditText txtPhutDenDialog = dialogView.findViewById(R.id.txtPhutDenDialog);
        final EditText txtGioDiDialog = dialogView.findViewById(R.id.txtGioDiDialog);
        final EditText txtPhutDiDialog = dialogView.findViewById(R.id.txtPhutDiDialog);
        final EditText txtThemTienDialog = dialogView.findViewById(R.id.txtThemTienDialog);
        final TextView txtNgayDenDialog = dialogView.findViewById(R.id.txtNgayDenDialog);
        final TextView txtNgayDiDialog = dialogView.findViewById(R.id.txtNgayDiDialog);
        final TextView txtPriceDialog = dialogView.findViewById(R.id.txtPriceDialog);
        TextView txtSoNgayDialog = dialogView.findViewById(R.id.txtSoNgayDialog);
        TextView txtPriceRoomDialog = dialogView.findViewById(R.id.txtPriceRoomDialog);
        TextView txtRoomNumberDialog = dialogView.findViewById(R.id.txtRoomNumberDialog);
        TextView txtRoomTypeDialog = dialogView.findViewById(R.id.txtRoomTypeDialog);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        String  checkInDate = roomModels.getCheckInDate().split(" ")[1];
        String  checkInTime = roomModels.getCheckInDate().split(" ")[0];
        String  checkOutDate = roomModels.getCheckOutDate().split(" ")[1];
        String  checkOutTime = roomModels.getCheckOutDate().split(" ")[0];

        String hoursCheckIn = checkInTime.split(":")[0];
        String minuteCheckIn = checkInTime.split(":")[1];
        String hoursCheckOut = checkOutTime.split(":")[0];
        String minuteCheckOut = checkOutTime.split(":")[1];

        txtRoomTypeDialog.setText(roomActivity.dsType.get(roomModels.getRoomType()));
        txtRoomNumberDialog.setText(roomModels.getRoomNumber());
        txtNgayDenDialog.setText(checkInDate);
        txtNgayDiDialog.setText(checkOutDate);
        txtGioDenDialog.setText(hoursCheckIn);
        txtPhutDenDialog.setText(minuteCheckIn);
        txtGioDiDialog.setText(hoursCheckOut);
        txtPhutDiDialog.setText(minuteCheckOut);
        txtSoNguoiDialog.setText(roomModels.getNumberPeople()+"");

        int soNgay =  Handle.convertToInt(Handle.countTheDays(roomModels.getCheckInDate().split(" ")[1])+"");
        String ngayO ="";
        int numDay = 0;
        if (soNgay < 0){
            ngayO = "Chưa đến";
        }else if (soNgay == 0){
            ngayO = "Ngày đầu";
            numDay = 1;
        }else{
            ngayO = soNgay+" ngày";
            numDay = soNgay;
        }
        final int gia = Handle.convertToInt(roomModels.getTotalPrice()) + numDay*Handle.convertToInt(roomModels.getRoomCharge());

        txtPriceDialog.setText(Handle.handleMoney(gia));
        txtSoNgayDialog.setText(ngayO);
        txtPriceRoomDialog.setText(Handle.handleMoney(numDay*Handle.convertToInt(roomModels.getRoomCharge())));

        Cursor cursor = roomActivity.dbHelper.getData("SELECT * FROM "+FeedReaderContract.Customer.TABLE_NAME+
                " WHERE "+FeedReaderContract.Customer.COLUMN_ID_ROOM+" = "+roomModels.getId());
        String name="";
        String CMT="";
        String phone="";

        int i = cursor.getCount();
        while (cursor.moveToNext()){
            i--;
            String n = "";
            if (i<0) n ="\n";
            name += cursor.getString(1)+n;
            CMT += cursor.getString(3)+n;
            phone += cursor.getString(2)+n;

        }
        txtNameDialog.setText(name);
        txtPhoneDialog.setText(phone);
        txtCMTDialog.setText(CMT);

        str1 = sdfDate2.format(today.getTime());
        str2 = sdfDate2.format(tomorrow.getTime());

        btnChonNgayDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str1 = "";
                handleSelectDate(txtNgayDenDialog);
            }
        });

        btnChonNgayDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str2 = "";
                handleSelectDate(txtNgayDiDialog);
            }
        });

        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gioDen = txtGioDenDialog.getText().toString()+":"+txtPhutDenDialog.getText().toString();
                String gioDi = txtGioDiDialog.getText().toString()+":"+txtPhutDiDialog.getText().toString();
                String name = txtNameDialog.getText().toString();
                String CMT = txtCMTDialog.getText().toString();
                String phone = txtPhoneDialog.getText().toString();

                int totalPrice = Handle.convertToInt(txtThemTienDialog.getText().toString()) +
                        Handle.convertToInt(roomModels.getTotalPrice());
                int soNguoi = Handle.convertToInt(txtSoNguoiDialog.getText().toString());
                if (soNguoi==0) soNguoi = 1;

                String ngayDen = txtNgayDenDialog.getText().toString();
                String ngayDi = txtNgayDiDialog.getText().toString();

                if (str2.compareTo(str1)<0){
                    Toast.makeText(roomActivity , "Ngày đi không hợp lệ!", Toast.LENGTH_LONG).show();

                }else if (str2.compareTo(str1)==0
                        && txtGioDiDialog.getText().toString().compareTo(txtGioDenDialog.getText().toString())<=0){
                    Toast.makeText(roomActivity, "Cùng ngày thì giờ đi phải lớn hơn giờ đến!", Toast.LENGTH_LONG).show();

                }else {
                    roomActivity.dbHelper.queryData("UPDATE "+ FeedReaderContract.Room.TABLE_NAME+
                            " SET "+FeedReaderContract.Room.COLUMN_CHECK_IN_DATE+" = '"+gioDen+" "+ngayDen+
                            "', "+FeedReaderContract.Room.COLUMN_CHECK_OUT_DATE+" = '"+gioDi+" "+ngayDi+
                            "', "+FeedReaderContract.Room.COLUMN_TOTAL_PRICE+" = '"+totalPrice+
                            "', "+FeedReaderContract.Room.COLUMN_RESERVE+" = 1"+
                            ", "+FeedReaderContract.Room.COLUMN_NUM_PEOPLE+" = "+soNguoi+
                            " WHERE "+FeedReaderContract.Room.COLUMN_ID+" = "+roomModels.getId());
                    roomActivity.dbHelper.queryData("UPDATE "+ FeedReaderContract.Customer.TABLE_NAME +
                            " SET "+FeedReaderContract.Customer.TABLE_NAME+" = '"+name+
                            "' "+FeedReaderContract.Customer.COLUMN_PHONE+" = '"+phone+
                            "' "+FeedReaderContract.Customer.COLUMN_CMT+" = '"+CMT+
                            "' WHERE "+FeedReaderContract.Customer.COLUMN_ID_ROOM+" = "+roomModels.getId()+")");
                    roomActivity.getRoom();
                    alertDialog.cancel();
                }

            }
        });

        txtGioDenDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtGioDenDialog.getText().toString())>23){
                        txtGioDenDialog.setText("23");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPhutDenDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtPhutDenDialog.getText().toString())>59){
                        txtPhutDenDialog.setText("59");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtGioDiDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtGioDiDialog.getText().toString())>23){
                        txtGioDiDialog.setText("23");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPhutDiDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(txtPhutDiDialog.getText().toString())>59){
                        txtPhutDiDialog.setText("59");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout layoutItem;
        TextView txtSoPhong;
        TextView txtLoaiPhong;
        ImageView imgTrangThai;
        ImageView imgAdd;
        public ViewHolder(View itemView, int resource) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            txtSoPhong = itemView.findViewById(R.id.txtSoPhong);
            txtLoaiPhong = itemView.findViewById(R.id.txtLoaiPhong);
            imgTrangThai = itemView.findViewById(R.id.imgTrangThai);
            if (resource == resourceAdd) imgAdd = itemView.findViewById(R.id.imgAdd);
        }
    }
}
