package co.martketing.escanercedulas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

import co.martketing.escanercedulas.models.InfoTarjeta;
import co.martketing.escanercedulas.models.PersonData;

public class MyHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "coviddatabase.db";
    static final String TABLE_PERSONS="persons";
    static final String DOC_ID="doc_id";
    static final String NOMBRES="nombres";
    static final String APELLIDOS="apellidos";
    static final String FECHA_NAC="fecha_nac";
    static final String FECHA="fecha";
    static final String RH="rh";
    static final String GENERO="genero";
    static final String TEMPERARURE="temperatura";
    static final String CITY="ciudad";
    public MyHelper(Context context){
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+TABLE_PERSONS+" " +
                        "(id integer primary key,"+
                        DOC_ID + " text, "+
                        FECHA + " text, "+
                        NOMBRES+ " text,"+
                        APELLIDOS + " text, " +
                        FECHA_NAC + " text,"+
                        RH + " text,"+
                        GENERO + " text,"+
                        CITY + " text,"+
                        TEMPERARURE + " integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PERSONS);
        onCreate(db);
    }
    public boolean insertPerson (InfoTarjeta infoTarjeta, String temperatura, String fecha,String ciudad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FECHA, fecha);
        contentValues.put(DOC_ID, infoTarjeta.getCedula());
        contentValues.put(NOMBRES, infoTarjeta.getPrimerNombre()+" "+ infoTarjeta.getSegundoNombre());
        contentValues.put(APELLIDOS, infoTarjeta.getPrimerApellido() +" " +infoTarjeta.getSegundoApellido());
        contentValues.put(FECHA_NAC, infoTarjeta.getFechaNacimiento());
        contentValues.put(RH, infoTarjeta.getRh());
        contentValues.put(GENERO, infoTarjeta.getSexo());
        contentValues.put(TEMPERARURE, temperatura);
        contentValues.put(CITY, ciudad);
        db.insert(TABLE_PERSONS, null, contentValues);
        Log.d("Insert Person","data"+infoTarjeta.toString());
        return true;
    }
    public Cursor getDataWithDate(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_PERSONS+" where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_PERSONS);
        return numRows;
    }
    public boolean updatePerson (Integer id, String doc_id, String nombre, String apellidos, String fecha,String fecha_nac, String rh, String genero,String temperatura,String ciudad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FECHA, fecha);
        contentValues.put(DOC_ID, doc_id);
        contentValues.put(NOMBRES, nombre);
        contentValues.put(APELLIDOS, apellidos);
        contentValues.put(FECHA_NAC, fecha_nac);
        contentValues.put(RH, rh);
        contentValues.put(GENERO, genero);
        contentValues.put(TEMPERARURE, temperatura);
        contentValues.put(CITY, ciudad);
        db.update(TABLE_PERSONS, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public Integer deletePerson (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PERSONS,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<PersonData> getAllPerson() {
        ArrayList<PersonData> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_PERSONS, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            PersonData person=new PersonData();
            person.setId(res.getInt(res.getColumnIndex("id")));
            person.setDoc_id(res.getString(res.getColumnIndex(DOC_ID)));
            person.setNames(res.getString(res.getColumnIndex(NOMBRES)));
            person.setLnames(res.getString(res.getColumnIndex(APELLIDOS)));
            person.setBirth_date(res.getString(res.getColumnIndex(FECHA_NAC)));
            person.setDate(res.getString(res.getColumnIndex(FECHA)));
            person.setRh(res.getString(res.getColumnIndex(RH)));
            person.setTempe(res.getString(res.getColumnIndex(TEMPERARURE)));
            person.setGenre(res.getString(res.getColumnIndex(GENERO)));
            person.setCiudad_procedencia(res.getString(res.getColumnIndex(CITY)));
            array_list.add(person);
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<PersonData> getPersonWithRangeDate(String dateFrom,String dateTo) {
        ArrayList<PersonData> array_list = new ArrayList<>();
        Log.d("getPersonWithDataRange","From: "+ dateFrom+ " to: "+ dateTo);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_PERSONS + " where "+FECHA + " between " +dateFrom + " and "+ dateTo, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            PersonData person=new PersonData();
            person.setId(res.getInt(res.getColumnIndex("id")));
            person.setDoc_id(res.getString(res.getColumnIndex(DOC_ID)));
            person.setNames(res.getString(res.getColumnIndex(NOMBRES)));
            person.setLnames(res.getString(res.getColumnIndex(APELLIDOS)));
            person.setBirth_date(res.getString(res.getColumnIndex(FECHA_NAC)));
            person.setDate(res.getString(res.getColumnIndex(FECHA)));
            person.setRh(res.getString(res.getColumnIndex(RH)));
            person.setTempe(res.getString(res.getColumnIndex(TEMPERARURE)));
            person.setGenre(res.getString(res.getColumnIndex(GENERO)));
            person.setCiudad_procedencia(res.getString(res.getColumnIndex(CITY)));
            Log.d("getPersonWithDataRange","Person: " + person.toString());
            array_list.add(person);
            res.moveToNext();
        }
        return  array_list;
    }
}
