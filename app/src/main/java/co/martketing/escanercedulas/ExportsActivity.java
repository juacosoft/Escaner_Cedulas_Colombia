package co.martketing.escanercedulas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.martketing.escanercedulas.adapters.PersonDataAdapter;
import co.martketing.escanercedulas.helper.MyHelper;
import co.martketing.escanercedulas.models.PersonData;

public class ExportsActivity extends AppCompatActivity {
    List<PersonData>persons=new ArrayList<>();
    MyHelper dbHelper;
    Context context;
    String edtFrom,edtTo;
    EditText fromAño,fromMes,fromDia,toAño,toMes,toDia;
    PersonDataAdapter adapter;
    private static final String TAG="ExportsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exports);
        context=this;
        dbHelper=new MyHelper(context);
        persons=dbHelper.getAllPerson();
        initEdit();
        if(persons.size()>0){
            init();
        }else{
            Toast.makeText(context,"No hay Personas almacenadas",Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btn_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if(validateEditDate()){
                        persons.removeAll(persons);
                        persons=dbHelper.getPersonWithRangeDate(edtFrom,edtTo);
                        adapter.notifyDataSetChanged();
                   }
            }
        });
    }
    private boolean validateEditDate(){
        if(fromDia.getText().toString().trim().isEmpty()){
            return false;
        }
        if(fromMes.getText().toString().trim().isEmpty()){
            return false;
        }
        if(fromAño.getText().toString().trim().isEmpty()){
            return false;
        }
        if(toDia.getText().toString().trim().isEmpty()){
            return false;
        }
        if(toMes.getText().toString().trim().isEmpty()){
            return false;
        }
        if(toAño.getText().toString().trim().isEmpty()){
            return false;
        }
        edtFrom =""+fromAño.getText().toString() +"-"+fromMes.getText().toString()+"-"+fromDia.getText().toString();
        edtTo=""+toAño.getText().toString()+"-"+toMes.getText().toString()+"-"+toDia.getText().toString();
        return true;
    }
    private void initEdit(){
        fromAño=(EditText)findViewById(R.id.edt_faño);
        fromMes=(EditText)findViewById(R.id.edt_fmes);
        fromDia=(EditText)findViewById(R.id.edt_fdia);
        toAño=(EditText)findViewById(R.id.edt_taño);
        toMes=(EditText)findViewById(R.id.edt_tmes);
        toDia=(EditText)findViewById(R.id.edt_tdia);
    }
    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvexport_persons);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PersonDataAdapter(persons,context);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new PersonDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PersonData obj, int position) {
                Toast.makeText(context,"Person:" + persons.get(position).toString(),Toast.LENGTH_SHORT).show();
                Log.d(TAG,"person"+persons.get(position).toString());
                //mChipsInput.addChip(obj.imageDrw, obj.name, obj.email);
                //dialog.hide();
            }
        });
    }
}