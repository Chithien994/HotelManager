package com.tcn.database;

import android.provider.BaseColumns;

/**
 * Created by MyPC on 14/03/2018.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class Room implements BaseColumns {
        public static final String TABLE_NAME = "room";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_RESERVE = "reserve";
        public static final String COLUMN_PLAN = "plan";
        public static final String COLUMN_CHECK_IN_DATE = "check_in_date";
        public static final String COLUMN_CHECK_OUT_DATE = "check_out_date";
        public static final String COLUMN_TOTAL_PRICE = "total_price";
        public static final String COLUMN_ROOM_CHARGE = "room_charge";
        public static final String COLUMN_MONEY_SERVICE = "money_service";
        public static final String COLUMN_NUM_PEOPLE = "num_people";
    }

    public static class Customer implements BaseColumns {
        public static final String TABLE_NAME = "customer";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_TEN= "ten";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_CMT = "cmt";
        public static final String COLUMN_ID_ROOM = "id_room";
    }

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_PASS = "password";
        public static final String COLUMN_PƠER = "power";
    }
}
