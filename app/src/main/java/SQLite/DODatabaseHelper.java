package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DODatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MetrocemDB.db";
    public static final String TABLE_NAME = "DOList";
    public static final String ID = "ID";
    public static final String DO_ID = "DO_Id";
    public static final String TYPE = "Type";
    public static final String ACTUAL_BAG_QTY = "Actual_Bag_Qty";
    public static final String UNIT_PRICE = "Unit_Price";
    public static final String DO_NUMBER = "Do_Number";
    public static final String DELIVERY_MODE = "Delivery_Mode";
    public static final String APPROVED_AT = "Approved_at";
    public static final String STATUS = "Status";
    public static final String CREATED_AT = "Created_at";
    public static final String EMPLOYEE_NAME = "Employee_Name";
    public static final String DEALER_NAME = "Dealer_Name";
    public static final String PRODUCT_NAME = "Product_Name";
    public static final String NUMBER_OF_CHALLAN = "Number_of_Challan";

    public DODatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY, DO_Id INTEGER, Type String, Actual_Bag_Qty INTEGER, Unit_Price INTEGER, Do_Number STRING, Delivery_Mode STRING, Approved_at STRING, Status STRING, Created_at STRING, Employee_Name STRING, Dealer_Name STRING, Product_Name STRING, Number_of_Challan INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(Integer do_id, String type, Integer actual_bag_qty, Integer unit_price, String do_number, String delivery_mode, String approved_at, String status, String created_at, String employee_name, String dealer_name, String product_name, Integer number_of_challan){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DO_ID", do_id);
        values.put("DO_ID", type);
        values.put("DO_ID", actual_bag_qty);
        values.put("DO_ID", unit_price);
        values.put("DO_ID", do_number);
        values.put("DO_ID", delivery_mode);
        values.put("DO_ID", approved_at);
        values.put("DO_ID", status);
        values.put("DO_ID", created_at);
        values.put("DO_ID", employee_name);
        values.put("DO_ID", dealer_name);
        values.put("DO_ID", product_name);
        values.put("DO_ID", number_of_challan);
        long result = db.insert(TABLE_NAME, null, values);

        if (result == -1)
            return false;
        else
            return true;

    }

    public Cursor getALlDOData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+TABLE_NAME, null);
        return res;
    }
}
