package tw.org.iii.besttodo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ActMain extends AppCompatActivity {

    ArrayList<String> num = new ArrayList<String>();
    int del=-1;

    private View.OnClickListener btnNew_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(txtTodo.getText().toString().length()>0&&txtDate.getText().toString().length()>0){
                SharedPreferences table=getSharedPreferences("tTodo",0);
                int count=table.getInt("count",0);
                count++;
                table.edit().putInt("count",count).commit();
                String keyT="T"+String.valueOf(count);
                String keyD="D"+String.valueOf(count);

                table.edit().putString(keyT,txtTodo.getText().toString()).commit();
                table.edit().putString(keyD,txtDate.getText().toString()).commit();

                txtTodo.setText("");
                txtDate.setText("");
                Toast.makeText(ActMain.this, "新增成功", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(ActMain.this,"請輸入代辦工作或日期才可新增",Toast.LENGTH_LONG).show();
            }

        }
    };
    private View.OnClickListener btnList_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i=0;i<num.size();i++){
                num.clear();
            }
            ArrayList<String> list = new ArrayList<String>();

            SharedPreferences table = getSharedPreferences("tTodo", 0);
            int count = table.getInt("count", 0);
            if (count == 0) {
                Toast.makeText(ActMain.this, "沒有任何代辦工作", Toast.LENGTH_LONG).show();
                return;
            }

            for (int i = 0; i <= count; i++) {
                String keyT = "T" + String.valueOf(i);
                String keyD = "D" + String.valueOf(i);
                if (table.contains(keyT)) {
                    list.add(table.getString(keyT, "") + "\r\n" +
                            table.getString(keyD, ""));
                    num.add(String.valueOf(i));
                }
            }

            AlertDialog.Builder message = new AlertDialog.Builder(ActMain.this);
            message.setTitle("尚有" + String.valueOf(list.size()) + "件工作未完成")
                    .setItems(list.toArray(new String[list.size()]), btnselect_click)
                    .create().show();


        }
    };

    private DialogInterface.OnClickListener btnselect_click=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            SharedPreferences table=getSharedPreferences("tTodo",0);
            txtTodo.setText(table.getString("T"+num.get(i),""));
            txtDate.setText(table.getString("D"+num.get(i),""));
            del=i;
        }
    };


    private View.OnClickListener btnDelete_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(del!=-1){
                SharedPreferences table=getSharedPreferences("tTodo",0);
                table.edit().remove("T"+num.get(del)).commit();
                table.edit().remove("D"+num.get(del)).commit();
                txtTodo.setText("");
                txtDate.setText("");
                del=-1;
                Toast.makeText(ActMain.this, "刪除成功", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(ActMain.this, "請選擇要刪除的事項", Toast.LENGTH_LONG).show();
            }

        }
    };


    DatePickerDialog.OnDateSetListener onDate_click=new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            txtDate.setText(String.valueOf(i)+"/"+
                    String.valueOf(i1+1)+"/"+
                    String.valueOf(i2));

        }
    };

    private View.OnClickListener btnTime_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar today=Calendar.getInstance();
            DatePickerDialog message=new DatePickerDialog(
                    ActMain.this,onDate_click,
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DATE)

            );
            message.show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);
        InitialComponent();
    }

    private void InitialComponent() {
        btnNew=(Button)findViewById(R.id.btnNew);
        btnNew.setOnClickListener(btnNew_click);
        btnList=(Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(btnList_click);
        btnTime=(Button)findViewById(R.id.btnTime);
        btnTime.setOnClickListener(btnTime_click);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(btnDelete_click);
        txtTodo=(EditText)findViewById(R.id.txtTodo);
        txtDate=(EditText)findViewById(R.id.txtDate);

    }
    Button btnNew;
    Button btnList;
    Button btnTime;
    Button btnDelete;
    EditText txtTodo;
    EditText txtDate;
}
