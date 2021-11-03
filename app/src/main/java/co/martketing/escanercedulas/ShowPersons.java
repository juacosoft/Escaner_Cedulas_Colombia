package co.martketing.escanercedulas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import co.martketing.escanercedulas.adapters.PersonDataAdapter;
import co.martketing.escanercedulas.helper.MyHelper;
import co.martketing.escanercedulas.models.PersonData;
import co.martketing.escanercedulas.utils.ViewAnimation;

public class ShowPersons extends AppCompatActivity {
    Context context;
    MyHelper dbHelper;
    private List<PersonData> persons = new ArrayList<>();
    private static final String TAG="ShowPersons";
    private boolean rotate = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_persons);
        context=this;
        dbHelper=new MyHelper(context);

        persons=dbHelper.getAllPerson();
        if(persons.size()>0){
            init();
        }else{
            Toast.makeText(context,"No hay Personas almacenadas",Toast.LENGTH_SHORT).show();
        }
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final FloatingActionButton fab_export = (FloatingActionButton) findViewById(R.id.fab_export);
        final FloatingActionButton fab_scan = (FloatingActionButton) findViewById(R.id.fab_scan);
        ViewAnimation.initShowOut(fab_export);
        ViewAnimation.initShowOut(fab_scan);
        ((FloatingActionButton) findViewById(R.id.fab_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate = ViewAnimation.rotateFab(v, !rotate);
                if (rotate) {
                    ViewAnimation.showIn(fab_export);
                    ViewAnimation.showIn(fab_scan);
                } else {
                    ViewAnimation.showOut(fab_export);
                    ViewAnimation.showOut(fab_scan);
                }
            }
        });
        fab_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call export activity
            }
        });
        fab_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowPersons.this,ScanActivity.class));

            }
        });


    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_persons);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PersonDataAdapter _adapter = new PersonDataAdapter(persons,context);
        recyclerView.setAdapter(_adapter);
        _adapter.setOnItemClickListener(new PersonDataAdapter.OnItemClickListener() {
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