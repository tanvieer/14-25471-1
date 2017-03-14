package calculator.test.tanvir.labcalculatortest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private TextView _screen;
    private String display = "";
    private String currentOperator = "";
    private String result = "";
    private boolean dotChecker = false;

    DatabaseHelper myDb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("calcDB","constructor create from mainactivity on create before constructor called");
        myDb = new DatabaseHelper(this);
        Log.d("calcDB","constructor create from mainactivity on create after constructor called");
        _screen = (TextView)findViewById(R.id.display);
        _screen.setText(display);

    }




//---------------------------------------------Private Functions------------------------------------
    private void updateScreen(){
        _screen.setText(display);
    }

    private void clear(){
        display = "";
        currentOperator = "";
        result = "";
        dotChecker = false;
    }

    private double operate(String a, String b, String op){

        switch(op){
            case "+": return Double.valueOf(a) + Double.valueOf(b);

            case "-": return Double.valueOf(a) - Double.valueOf(b);

            case "*": return Double.valueOf(a) * Double.valueOf(b);

            case "/": try{
                return Double.valueOf(a) / Double.valueOf(b);
            } catch (Exception e){
                Log.d("Calc", e.getMessage());
            }
            default: return -1;
        }
    }


    private boolean getResult(){
        dotChecker = false;
        if(currentOperator == "") return false;
        String[] operation = display.split(Pattern.quote(currentOperator));
        if(operation.length < 2) return false;
        result = String.valueOf(operate(operation[0], operation[1], currentOperator));

        String data = display+" = "+ String.valueOf(result);
        if(myDb.insertData(data)){
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Data cannot be inserted", Toast.LENGTH_SHORT).show();

        return true;
    }

    private boolean isOperator(char op){
        switch (op){
            case '+':
            case '-':
            case '*':
            case '/':return true;
            default: return false;
        }
    }




//------------------------------------Button Functions----------------------------------------------


    public void onClickNumber( View v){

        if(result != ""){
            clear();
            updateScreen();
        }
        Button b = (Button) v;


        String temp = b.getText().toString();

        if(temp.indexOf(".")>-1){
            if(!dotChecker) display += b.getText();
            dotChecker = true;
            Log.d("dotcheck", "dot paise -_-");
        }
        else display += b.getText();
        updateScreen();
    }



    public void onClickOperator(View v){
        dotChecker = false;

        if(display == "") return;

        Button b = (Button)v;

        if(result != ""){
            String _display = result;
            clear();
            display = _display;
        }

        if(currentOperator != ""){
            Log.d("LastOP", ""+display.charAt(display.length()-1));
            if(isOperator(display.charAt(display.length()-1))){
                display = display.replace(display.charAt(display.length()-1), b.getText().charAt(0));
                updateScreen();
                return;
            }else{
                getResult();
                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }
        display += b.getText();
        currentOperator = b.getText().toString();
        updateScreen();
    }



    public void onClickClear(View v){
        clear();
        updateScreen();
    }

    public void onClickEqual(View v){
        if(display == "") return;
        if(!getResult()) return;



        _screen.setText(display + "\n" + String.valueOf(result));
    }

    //---------------------------------Memory Function----------------------------------------------


    public void onClickMemoryPlusButton(View v){
        SharedPreferences sharedpreferences = getSharedPreferences("Mem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();


        Log.d("test","Mem called before1");
        Float oldValue = Float.parseFloat(sharedpreferences.getString("Mem","0"));
        Log.d("test","Mem called before2");
        String disp =  parseNumber();
        float newValue =  Float.parseFloat(disp);
        Log.d("test","Mem called after");
        try{
            oldValue += newValue;
            display =  Float.toString(oldValue);
            editor.putString("Mem", display);
            updateScreen();

            Toast.makeText(this, display+" Saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            editor.putString("Mem", "0");
            Toast.makeText(this, "0 Saved", Toast.LENGTH_SHORT).show();
            display =  "0";
            updateScreen();
        }
        editor.apply();

    }


    public void onClickMemoryMinusButton(View v){
        SharedPreferences sharedpreferences = getSharedPreferences("Mem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();


        // Log.d("test","Mem called before1");
        Float oldValue = Float.parseFloat(sharedpreferences.getString("Mem","0"));
        // Log.d("test","Mem called before2");
        String disp =  parseNumber();
        float newValue =  Float.parseFloat(disp);
        // Log.d("test","Mem called after");
        try{
            oldValue -= newValue;
            display =  Float.toString(oldValue);
            editor.putString("Mem", display);
            updateScreen();

            Toast.makeText(this, display+" Saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            editor.putString("Mem", "0");
            Toast.makeText(this, "0 Saved", Toast.LENGTH_SHORT).show();
            display =  "0";
            updateScreen();
        }
        editor.apply();

    }

    public void onClickMemoryClean(View v){
        SharedPreferences sharedpreferences = getSharedPreferences("Mem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Mem","0");
        editor.apply();
        Toast.makeText(this, "Memory Cleaned", Toast.LENGTH_SHORT).show();
    }
    public void onClickMemoryRecall(View v){
        SharedPreferences sharedpreferences = getSharedPreferences("Mem", Context.MODE_PRIVATE);
        display = sharedpreferences.getString("Mem","");
        updateScreen();
    }


    private String parseNumber(){
        StringBuilder builder = new StringBuilder("0");

        if(display == "") return "0";

        if(currentOperator == "") return display;
        else {
            for(int i = 0; i< display.length();i++){
                char a = display.charAt(i);
                if(a>='0' || a <='9' ){
                    builder.append(a);
                    Log.d("MemoryBuilder",builder.toString());
                }
                else {
                    Log.d("ReturnMemoryBuilder",builder.toString());
                    return builder.toString();
                }
            }
            return "0";
        }
    }

    //---------------------------------Memory Function End----------------------------------------------


    //------------------Activity Switching Button---------------------------------------

    public void onClickHistory(View v){
        Log.d("intentest","first line");

       /* //Intent i = new Intent(getApplicationContext(), .class);

        Log.d("intentest","second line");

        //i.putExtra("Value1", "Android By Javatpoint");

        Log.d("intentest","third line");

       // i.putExtra("Value2", "Simple Tutorial");

        Log.d("intentest","fourth line");
        // Set the request code to any code you like, you can identify the
        // callback via this code
        //startActivity(i);
*/



        Toast.makeText(this, "History Button Clicked", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(MainActivity.this, HistoryActivity.class));

        Log.d("intentest","fifth line");
    }






}
