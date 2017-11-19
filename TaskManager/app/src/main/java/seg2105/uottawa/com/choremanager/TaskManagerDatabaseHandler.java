package seg2105.uottawa.com.choremanager;

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


    // User Table Column Names
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";


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
