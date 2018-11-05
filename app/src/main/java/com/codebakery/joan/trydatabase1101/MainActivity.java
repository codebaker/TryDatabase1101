package com.codebakery.joan.trydatabase1101;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MemberDbHelper dbHelper;
    private SQLiteDatabase mdb;
    Cursor cursor;

    TextView textViewId;
    TextView textViewCount;
    EditText editTextCountry;
    EditText editTextCapital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. activity_main.xml 화면을 그리고 와서...

        // 2. 버튼 동작이 있으니까 핸들러를 달아줍니다.
        ((Button) findViewById(R.id.buttonInsert)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonRead)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonUpdate)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonDelete)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonSearch)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonAddVisit)).setOnClickListener(this);
        // 3. this 가 에러날 꺼예요. --> implements View.OnClickListener 를 넣어주세요.
        // 4. 이제 this는 괜찮은데 implements View.OnClickListener 가 에러납니다.
        // 5. 풍선을 클릭하거나 Alt+Enter 해서 implement method를 선택해서 onClick(View v)를 오버라이드 해줍니다.

        textViewId = findViewById(R.id.textViewId);
        textViewCount = findViewById(R.id.textViewCount);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextCapital = findViewById(R.id.editTextCapital);

        // DB 연결해주고 테이블이 없으면 만들어줄 객체를 가져옵니다.
        dbHelper = new MemberDbHelper(this);
        //MemberDbHelper가 상속받은 SQLiteOpenHelper에 있는 getWritableDatabase() 메서드를 이용합니다.
        // 쿼리문을 실행해줄 객체도 가겨옵니다.
        mdb = dbHelper.getWritableDatabase();
    }

    // 6. 여기에 DB에 저장하는 코드를 넣어줍니다.
    @Override
    public void onClick(View v) {
        // 1. 먼저 DB에 넣을 값이 필요하니까 화면에 입력한 값을 가져옵니다.
        //    따로 변수 선언 하지 않고 바로 String으로 가져옵니다.

        String country = editTextCountry.getText().toString();
        String capital = editTextCapital.getText().toString();
        switch (v.getId()){
            case R.id.buttonSearch :
                searchByCountry(country);
                break;
            case R.id.buttonAddVisit :
                addVisitCount(textViewId.getText().toString());
                break;
            case R.id.buttonInsert:
                insertRecord(country,capital);
                break;
            case R.id.buttonRead :
                readData(country);
                break;
            case R.id.buttonUpdate:
                updateRecord(country,capital);
                break;
            case R.id.buttonDelete :
                deleteRecord(capital);
                break;
            
        }
    }

    private void addVisitCount(String pkid) {
        String query = "INSERT INTO awe_country_visitedcount VALUES (" + pkid + ")";
        mdb.execSQL(query);

    }

    private void searchByCountry(String pkid) {
        String query = "SELECT pkid, country, capital, ifnull(vcount,0) visitedTotal " +
                        "FROM (SELECT * FROM awe_country WHERE country='"+pkid+"') a " +
                        "LEFT JOIN (SELECT fkid, count(*) vcount FROM awe_country_visitedcount GROUP BY fkid) b " +
                        "ON pkid = fkid";
        cursor = mdb.rawQuery(query,null);
        int count = cursor.getCount();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("pkid"));
            String visitedTotal = cursor.getString(cursor.getColumnIndex("visitedTotal"));
            String country = cursor.getString(cursor.getColumnIndex("country"));
            String capital = cursor.getString(cursor.getColumnIndex("capital"));
            textViewId.setText("pkid : " + id);
            textViewCount.setText("visitedTotal : "+visitedTotal);
            editTextCountry.setText(country);
            editTextCapital.setText(capital);
        }else{
            textViewId.setText("");
            textViewCount.setText("");
            editTextCountry.setHint("입력되지 않은 나라");
            editTextCountry.setText("");
            editTextCapital.setText("");
        }
    }

    private void insertRecord(String editTextCountry,String editTextCapital) {
        //쿼리문을 만듭니다.
        //insert into awe_country values(null,'Korea','Seoul');
        String query = "INSERT INTO "+ "awe_country" + " VALUES(" + null + ",'" + editTextCountry + "','" + editTextCapital + "')";
        mdb.execSQL(query);
    }

    private void readData(String editTextCountry) {
        //쿼리문을 만듭니다.
        //String query = "select * from awe_country"
        String query = "SELECT * FROM awe_country ORDER BY pkid DESC";
        Cursor cursor = mdb.rawQuery(query,null);
        StringBuilder resultStr = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("pkid"));
            String country = cursor.getString(cursor.getColumnIndex("country"));
            String capital = cursor.getString(cursor.getColumnIndex("capital"));

            resultStr.append(id + " : " + country + "-" + capital + "\n");
        }
        ((TextView)findViewById(R.id.textViewResult)).setText(resultStr);

    }

    private void updateRecord(String editTextCountry, String editTextCapital) {
    }

    private void deleteRecord(String editTextCountry) {
    }
}
