package com.codebakery.joan.trydatabase1101;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MemberDbHelper dbHelper;
    private SQLiteDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. activity_main.xml 화면을 그리고 와서...

        // 2. 버튼 동작이 있으니까 핸들러를 달아줍니다.
        ((Button) findViewById(R.id.buttonSave)).setOnClickListener(this);
        // 3. this 가 에러날 꺼예요. --> implements View.OnClickListener 를 넣어주세요.
        // 4. 이제 this는 괜찮은데 implements View.OnClickListener 가 에러납니다.
        // 5. 풍선을 클릭하거나 Alt+Enter 해서 implement method를 선택해서 onClick(View v)를 오버라이드 해줍니다.
    }

    // 6. 여기에 DB에 저장하는 코드를 넣어줍니다.
    @Override
    public void onClick(View v) {

        // 1. 먼저 DB에 넣을 값이 필요하니까 화면에 입력한 값을 가져옵니다.
        //    따로 변수 선언 하지 않고 바로 String으로 가져옵니다.
        String editTextCountry = ((EditText)findViewById(R.id.editTextCountry)).getText().toString();
        String editTextCapital = ((EditText)findViewById(R.id.editTextCapital)).getText().toString();

        //위에걸로 쿼리문도 미리 만들어둡니다.
        //insert into awe_country values(null,'Korea','Seoul');
        String query = "INSERT INTO "+ "awe_country" + " VALUES(" + null + ",'" + editTextCountry + "','" + editTextCapital + "');";
        // 2. DB 연결해주고 테이블이 없으면 만들어줄 객체를 가져옵니다.
        dbHelper = new MemberDbHelper(this);
        //MemberDbHelper가 상속받은 SQLiteOpenHelper에 있는 getWritableDatabase() 메서드를 이용합니다.
        // 3.  쿼리문을 실행해줄 객체도 가겨옵니다.
        mdb = dbHelper.getWritableDatabase();
        mdb.execSQL(query);
    }
}
