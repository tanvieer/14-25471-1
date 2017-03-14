package calculator.test.tanvir.labcalculatortest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private TextView _ScrollScreen;
    private DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        _ScrollScreen = (TextView)findViewById(R.id.historytext);
        myDb = new DatabaseHelper(this);
        Log.d("HistoryClass","on create history activity " );







        String data="";

        List<String> dataList =  myDb.getHistory();
        Log.d("HistoryClass","on Resume history activity 2" );

        for(String s : dataList) {
            data += s;
        }

        Log.d("HistoryClass","on Resume history activity 3" );
        if(data!="")
        _ScrollScreen.setText(data);






    }




    /*@Override
    public void onResume(){
        //will be executed onResume
        String data="";

        List<String> dataList =  myDb.getHistory();
        Log.d("HistoryClass","on Resume history activity 2" );

//        for(String s : dataList) {
//            data += s;
//        }

        Log.d("HistoryClass","on Resume history activity 3" );
       // _ScrollScreen.setText(data);
    }*/

    public void onClickBack(View v){
        Toast.makeText(this, "BackClicked", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(HistoryActivity.this, MainActivity.class));
    }
}
