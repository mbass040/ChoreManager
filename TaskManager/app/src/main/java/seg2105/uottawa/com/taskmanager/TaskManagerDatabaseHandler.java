package seg2105.uottawa.com.taskmanager;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
/**
 * Created by Vincent on 11/19/2017.
 */

public class TaskManagerDatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "taskManagerDB.db";
    // Table name
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TASK = "task";
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_CART_ITEM = "cart_item";

    // User Table Column Names
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";

    // Task Table Column Names
    public static final String TASK_ID = "task_id";
    public static final String TASK_NAME = "task_name";
    public static final String TASK_NOTES = "task_notes";
    public static final String TASK_DEADLINE = "task_deadline";
    public static final String TASK_POINT_VALUE = "task_point_value";
    public static final String TASK_STATUS = "task_status";

    // Item Table Column Names
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "item_name";

    // Cart Item Column Names
    public static final String CART_ITEM_ID = "cart_item_id";
    public static final String CART_ITEM_TYPE = "cart_item_type";

    public TaskManagerDatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
