package seg2105.uottawa.com.taskmanager;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.CartItem;
import seg2105.uottawa.com.taskmanager.source.Item;
import seg2105.uottawa.com.taskmanager.source.Task;
import seg2105.uottawa.com.taskmanager.source.User;

public class TaskManagerDatabaseHandler extends SQLiteOpenHelper {
    public enum DBItemType {Equipment, CartItem}

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "taskManagerDB.db";
    // Table name
    private static final String TABLE_USER = "users";
    private static final String TABLE_TASK = "task";
    private static final String TABLE_ITEM = "item";
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
    private static final String TASK_DURATION = "task_duration";
    private static final String TASK_POINT_VALUE = "task_point_value";
    private static final String TASK_STATUS = "task_status";
    private static final String TASK_CREATED_BY = "task_created_by";
    private static final String TASK_ASSIGNED_TO = "task_assigned_to";

    // Item Table Column Names
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";

    // Cart Item Column Name
    private static final String CART_ITEM_TYPE = "cart_item_type";

    // Task Equipment Column Names
    private static final String TASK_EQUIPMENT_ID = "task_equipment_id";
    private static final String TASK_EQUIPMENT_TASK_ID = "task_equipment_task_id"; // Foreign Key to Task table
    private static final String TASK_EQUIPMENT_EQUIPMENT_ID = "task_equipment_equipment_id"; // Foreign Key to Equipment table

    // Shopping cart Column Name
    private static final String SHOPPING_CART_ID = "shopping_cart_id";

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
                + ITEM_ID + " INTEGER PRIMARY KEY, "
                + ITEM_NAME + " TEXT, "
                + CART_ITEM_TYPE + " INTEGER)";
        db.execSQL(CREATE_ITEM_TABLE);

        // Task table create query
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                + TASK_ID + " INTEGER PRIMARY KEY, "
                + TASK_NAME + " TEXT, "
                + TASK_NOTES + " TEXT, "
                + TASK_DEADLINE + " TEXT, "
                + TASK_DURATION + " INTEGER, "
                + TASK_POINT_VALUE + " INTEGER, "
                + TASK_STATUS + " INTEGER, "
                + TASK_CREATED_BY + " INTEGER, "
                + TASK_ASSIGNED_TO + " INTEGER, "
                // Foreign Key constraints
                + "FOREIGN KEY(" + TASK_CREATED_BY + ") REFERENCES " + TABLE_USER + "(" + USER_ID + "), "
                + "FOREIGN KEY(" + TASK_ASSIGNED_TO + ") REFERENCES " + TABLE_USER + "(" + USER_ID + "))";
        db.execSQL(CREATE_TASK_TABLE);

        // Task equipment table create query
        String CREATE_TASK_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_TASK_EQUIPMENT + "("
                + TASK_EQUIPMENT_ID + " INTEGER PRIMARY KEY, "
                + TASK_EQUIPMENT_TASK_ID + " INTEGER, "
                + TASK_EQUIPMENT_EQUIPMENT_ID + " INTEGER, "
                // Foreign Key constraints
                + "FOREIGN KEY(" + TASK_EQUIPMENT_TASK_ID + ") REFERENCES " + TABLE_TASK + "(" + TASK_ID + "), "
                + "FOREIGN KEY(" + TASK_EQUIPMENT_EQUIPMENT_ID + ") REFERENCES " + TABLE_ITEM + "(" + ITEM_ID + "))";
        db.execSQL(CREATE_TASK_EQUIPMENT_TABLE);

        // Task equipment table create query
        String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + TABLE_SHOPPING_CART + "("
                + SHOPPING_CART_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_SHOPPING_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
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
    public void insertItem(DBItemType dbType, String name, CartItem.ItemType type){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cvItem = new ContentValues();
        cvItem.put(ITEM_NAME, name);

        // Only insert a value in the ITEM_TYPE column if this item is a CartItem; Equipment items don't have a specified type.
        if (dbType == DBItemType.CartItem)
            cvItem.put(CART_ITEM_TYPE, type.ordinal());
        else
            cvItem.putNull(CART_ITEM_TYPE);

        //inserting row
        db.insert(TABLE_ITEM, null, cvItem);
        db.close();
    }

    public List<Item> getItems (boolean isEquipment) {
        // Only Equipment items have the ITEM_TYPE column as null
        String query = "SELECT " + ITEM_ID + ", " + ITEM_NAME + " FROM " + TABLE_ITEM
                + " WHERE " + CART_ITEM_TYPE + " IS" + (!isEquipment ? " NOT" : "") + " NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursItem = db.rawQuery(query, null);

        List<Item> items = new ArrayList<>();

        if (cursItem.moveToFirst()) {
            do {
                Item item;
                if (!isEquipment) { // AKA if we only want cart items
                    CartItem.ItemType type;
                    if (cursItem.getInt(2) == 0)
                            type = CartItem.ItemType.Grocery;
                    else
                        type = CartItem.ItemType.Material;

                    item = new CartItem(cursItem.getInt(0), cursItem.getString(1), type);
                }
                else
                    item = new Item(cursItem.getInt(0), cursItem.getString(1));

                items.add(item);
            } while (cursItem.moveToNext());
        }

        //closing connection
        cursItem.close();
        db.close();
        return items;
    }

    public void insertTaskEquipment (int taskID, int equipmentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cvTaskEquipment = new ContentValues();

        cvTaskEquipment.put(TASK_EQUIPMENT_TASK_ID, taskID);
        cvTaskEquipment.put(TASK_EQUIPMENT_EQUIPMENT_ID, equipmentID);

        //inserting row into associative entity TaskEquipment
        db.insert(TABLE_TASK_EQUIPMENT, null, cvTaskEquipment);
        db.close();
    }
    public void insertItem(CartItem.ItemType itemType, String itemName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cvItem = new ContentValues();

        cvItem.put(ITEM_NAME, itemName);
        // the itemType parameter is used only for CartItem objects, so if we're inserting an equipment
        // the CART_ITEM_TYPE column will be null.
        if(itemType == null){
            cvItem.putNull(CART_ITEM_TYPE);
        }else{
            cvItem.put(CART_ITEM_TYPE, itemType.ordinal());
        }
        db.insert(TABLE_ITEM, null, cvItem);
        db.close();

    }//insertItem

    public void deleteTaskEquipment (int taskID, int equipmentID) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete a task equipment where the taskID and equipmentID match its respective argument.
        db.delete(TABLE_TASK_EQUIPMENT, TASK_EQUIPMENT_TASK_ID + " = '" + taskID + "' AND " + TASK_EQUIPMENT_EQUIPMENT_ID + " = '" + equipmentID + "'", null);
        db.close();
    }


    // Gets the equipment associated to a task only when the task IDs match
    public List<Item> getTaskEquipment (int taskID) {
        String query = "SELECT " + ITEM_ID + ", " + ITEM_NAME + " FROM " + TABLE_ITEM
                        + " WHERE " + ITEM_ID + " IN ( " +
                                "SELECT " + TASK_EQUIPMENT_EQUIPMENT_ID + " FROM " + TABLE_TASK_EQUIPMENT
                                + " WHERE " + TASK_EQUIPMENT_TASK_ID + " = " + taskID
                        + " )";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursEquip = db.rawQuery(query, null);

        List<Item> equipment = new ArrayList<>();

        if (cursEquip.moveToFirst()) {
            do {
                Item item = new Item(cursEquip.getInt(0), cursEquip.getString(1));
                equipment.add(item);
            } while (cursEquip.moveToNext());
        }

        //closing connection
        cursEquip.close();
        db.close();
        return equipment;
    }

    public void insertTask (String name, String notes, String deadline, int hoursDuration, int pointValue, Task.TaskStatus status, int createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cvTask = new ContentValues();
        cvTask.put(TASK_NAME, name);
        cvTask.put(TASK_NOTES, notes);
        cvTask.put(TASK_DEADLINE, deadline);
        cvTask.put(TASK_DURATION, hoursDuration);
        cvTask.put(TASK_POINT_VALUE, pointValue);
        cvTask.put(TASK_STATUS, status.ordinal());
        //cvTask.put(TASK_CREATED_BY, createdBy); TODO: Add current user ID
        cvTask.putNull(TASK_ASSIGNED_TO); // Not assigned to anyone upon creation

        //inserting row
        db.insert(TABLE_TASK, null, cvTask);
        db.close();
    }

    public void updateTask (int ID, String notes, String deadline, int hoursDuration, int pointValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cvTask = new ContentValues();
        cvTask.put(TASK_NOTES, notes);
        cvTask.put(TASK_DEADLINE, deadline);
        cvTask.put(TASK_DURATION, hoursDuration);
        cvTask.put(TASK_POINT_VALUE, pointValue);

        db.update(TABLE_TASK, cvTask, TASK_ID + " = " + ID, null);
        db.close();
    }

    public List<Task> getTasks(boolean meOnly) {
        // Get tasks that are not 'Completed'
        String query = "SELECT * FROM " + TABLE_TASK + " WHERE " + TASK_STATUS + " <> 0";

        //if (meOnly)
            // TODO: use current user for WHERE clause

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursTasks = db.rawQuery(query, null);

        List<Task> tasks = new ArrayList<>();

        if (cursTasks.moveToFirst()) {
            do {
                Task.TaskStatus status;

                switch (cursTasks.getInt(5)) {
                    case 0:
                        status = Task.TaskStatus.Completed;
                        break;
                    case 1:
                        status = Task.TaskStatus.Assigned;
                        break;
                    case 2:
                        status = Task.TaskStatus.Postponed;
                        break;
                    default:
                        status = Task.TaskStatus.Unassigned;
                        break;
                }

                Task task = new Task(cursTasks.getInt(0), cursTasks.getString(1), cursTasks.getString(2), cursTasks.getString(3), cursTasks.getInt(4), cursTasks.getInt(5), status, cursTasks.getInt(6), cursTasks.getInt(7));
                tasks.add(task);
            } while (cursTasks.moveToNext());
        }

        //closing connection
        cursTasks.close();
        db.close();

        return tasks;
    }

    public Task getSpecificTask(int ID) {
        String query = "SELECT * FROM " + TABLE_TASK + " WHERE " + TASK_ID + " = " + ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursTasks = db.rawQuery(query, null);

        Task task = null;

        if (cursTasks.moveToFirst()) {
            Task.TaskStatus status;

            switch (cursTasks.getInt(5)) {
                case 0:
                    status = Task.TaskStatus.Completed;
                    break;
                case 1:
                    status = Task.TaskStatus.Assigned;
                    break;
                case 2:
                    status = Task.TaskStatus.Postponed;
                    break;
                default:
                    status = Task.TaskStatus.Unassigned;
                    break;
            }

            task = new Task(cursTasks.getInt(0), cursTasks.getString(1), cursTasks.getString(2), cursTasks.getString(3), cursTasks.getInt(4), cursTasks.getInt(5), status, cursTasks.getInt(6), cursTasks.getInt(7));
        }

        //closing connection
        cursTasks.close();
        db.close();

        return task;
    }

    //insert a new Cart Item to TABLE_CART_ITEM
//    public void insertCartItem(String cartItemName, int itemType){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cvCartItem = new ContentValues();
//        cvCartItem.put(ITEM_NAME, cartItemName);
//    }


    public List<String> getAllUsers(){
        List<String> userList = new ArrayList<>();
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

    public User getUser (int ID) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID + " = " + ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursUsers = db.rawQuery(query, null);

        User user = null;

        if(cursUsers.moveToFirst())
            user = new User(cursUsers.getInt(0), cursUsers.getString(1));

        //closing connection
        cursUsers.close();
        db.close();

        return user;
    }
}
