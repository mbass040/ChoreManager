package seg2105.uottawa.com.taskmanager;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 11/19/2017.
 */

public class TaskManagerDatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "taskManagerDB.db";
    // Table name
    private static final String TABLE_USER = "users";
    private static final String TABLE_TASK = "task";
    private static final String TABLE_ITEM = "item";
    private static final String TABLE_CART_ITEM = "cart_item";
    private static final String TABLE_TASK_EQUIPMENT = "task_equipment";
    private static final String TABLE_SHOPPING_CART = "shopping_cart";

    // User Table Column Names
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";

    // Task Table Column Names
    private static final String TASK_ID = "task_id";
    private static final String TASK_NAME = "task_name";
    private static final String TASK_NOTES = "task_notes";
    private static final String TASK_DEADLINE = "task_deadline";
    private static final String TASK_POINT_VALUE = "task_point_value";
    private static final String TASK_STATUS = "task_status";

    // Item Table Column Names
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";

    // Cart Item Column Names
    private static final String CART_ITEM_ID = "cart_item_id";
    private static final String CART_ITEM_TYPE = "cart_item_type";

    // Task equipment Column Name
    private static final String TASK_EQUIPMENT_ID = "task_equipment_id";

    // Shopping cart Column Name
    private static final String SHOPPING_CART_ID = "shopping_cart_id";

    List<String> userList = new ArrayList<String>();

    public TaskManagerDatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){ //creating tables
        // User table create query
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY," + USER_NAME + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Item table create query
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + ITEM_ID + " INTEGER PRIMARY KEY,"
                + CART_ITEM_ID + " INTEGER REFERENCE "
                + TABLE_CART_ITEM + ", "
                + ITEM_NAME + " TEXT)";
        db.execSQL(CREATE_ITEM_TABLE);

        // Cart Item table create query
        String CREATE_CART_ITEM_TABLE = "CREATE TABLE " + TABLE_CART_ITEM + "("
                + CART_ITEM_ID + " INTEGER PRIMARY KEY,"
                + ITEM_ID + " INTEGER REFERENCE "
                + CART_ITEM_TYPE + " TEXT)";
        db.execSQL(CREATE_CART_ITEM_TABLE);

        // Task table create query
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                + TASK_ID + " INTEGER PRIMARY KEY," + TASK_NAME + " TEXT," + TASK_NOTES + " TEXT,"
                + TASK_DEADLINE + " TEXT," + TASK_POINT_VALUE + " INTEGER," + TASK_STATUS + " TEXT)";
        db.execSQL(CREATE_TASK_TABLE);

        // Task equipment table create query
        String CREATE_TASK_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_TASK_EQUIPMENT + "("
                + TASK_EQUIPMENT_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_TASK_EQUIPMENT_TABLE);

        // Task equipment table create query
        String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + TABLE_SHOPPING_CART + "("
                + SHOPPING_CART_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_SHOPPING_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM);
        //Create tables again
        onCreate(db);
    }

    //inserting a new user to TABLE_USER
    public void insertUser(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cvUser = new ContentValues();
        cvUser.put(USER_NAME, name);

        //inserting row
        db.insert(TABLE_USER, null, cvUser);
        db.close();
    }

    //inserting a new Item to TABLE_ITEM
    public void insertItem(String item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cvItem = new ContentValues();
        cvItem.put(ITEM_NAME, item);

        //inserting row
        db.insert(TABLE_ITEM, null, cvItem);
        db.close();
    }

    //insert a new Cart Item to TABLE_CART_ITEM
//    public void insertCartItem(String cartItemName, int itemType){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cvCartItem = new ContentValues();
//        cvCartItem.put(ITEM_NAME, cartItemName);
//    }


    public List<String> getAllUsers(){
        //select all users
        String selectUser = "SELECT " + USER_NAME + " FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursUser = db.rawQuery(selectUser, null);

        // looping through all rows and adding to list
        if(cursUser.moveToFirst()){
            do {
                userList.add(cursUser.getString(0));
            } while (cursUser.moveToNext());
        } //if

        //closing connection
        cursUser.close();
        db.close();

        return userList;
    }
}
