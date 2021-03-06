package com.postfive.habit.view.habit;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.postfive.habit.R;
import com.postfive.habit.adpater.habittime.HabitTimeRecyclerViewAdapter;
import com.postfive.habit.db.Habit;
import com.postfive.habit.db.UserHabitDetail;
import com.postfive.habit.db.UserHabitRespository;
import com.postfive.habit.db.UserHabitState;
import com.postfive.habit.habits.HabitMaker;
import com.postfive.habit.habits.factory.HabitFactory;
import com.postfive.habit.view.myhabitlist.MyHabitListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class HabitActivity extends AppCompatActivity {


    private static final String TAG = "HabitActivity";

    private UserHabitRespository mUserHabitRespository;

    private HashMap<String, Integer> mUnitMap = new HashMap<>();
    private HabitFactory mHabitFactory;
    private UserHabitDetail mHabit;

    /* 화면 component */
    private TextView mHabitType ;               //목표
    private EditText mGoalEdtText ;             //목표
    private EditText mDayGoalEdtText;           // 일목표
    private EditText mOnceEdtText ;             // 일 회 수행 양(?)
    private Spinner mSpinnerUnit;               // 단위
    private TextView mUnitTextview;
    private int [] mDayofWeekToggleBtnId;       // 요일 버튼 id
    private ToggleButton[] mDayofWeekToggleBtn; // 요일 버튼
    private ToggleButton mEveryDayToggleBtn;    // 매일 버튼
    private ToggleButton mEveryWeekToggleBtn;   // 매주 버튼


    private ToggleButton mMorningTimeToggleBtn;     // 아침 버튼
    private ToggleButton mAfternoonTimeToggleBtn;   // 오후 버튼
    private ToggleButton mNightTimeToggleBtn;       // 저녁 버튼
    private ToggleButton mAllTimeToggleBtn;         // 하루종일 버튼

    private Button mAddTimeBtn;
    private RecyclerView mTimeRecyclerView ;
    private HabitTimeRecyclerViewAdapter mHabitTimeRecyclerViewAdapter;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        initComponent();

        processIntent();
    }

    protected void onNewIntent(Intent intent){
        processIntent();
        super.onNewIntent(intent);
    }

    private void processIntent() {
        Intent receivedIntent = getIntent();
        int habitCode = receivedIntent.getIntExtra("habit", 0);
        mHabit = (UserHabitDetail) receivedIntent.getSerializableExtra("object");


        if(mHabit == null) {

            Habit templeHabit = mHabitFactory.createHabit(habitCode);
            templeHabit.prepare();
           mHabit = new UserHabitDetail(templeHabit);

        }


        Log.d(TAG, "seq " +mHabit.getName()+" / " +Integer.toString(mHabit.getTime()));
        //TODO 수정인 경우 여기서 mHabit에 모든 값 setting  해야겟군
        setComponent(mHabit);
    }


        /* toolbar, action bar 버튼 클릭 이벤트 */
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onClickHome();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // 뒤로
    private void onClickHome(){
//        Intent intent = new Intent(this, MainActivity.class);
        //setResult(RESULT_CANCELED, intent);
//        startActivity(intent);
        finish();
    }
    private void goMyHabitList(){
        Intent intent = new Intent(this, MyHabitListActivity.class);

        startActivity(intent);
        finish();
    }

    /**
     *  initComponent
     *  화면 component set
     */
    private void initComponent() {

        // Toolbar 설정
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_habit);
        setSupportActionBar(myToolbar);

        // 액션바 뒤로가기 버튼
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //
        mHabitType = (TextView)findViewById(R.id.textView_habit_type);
        // 목표 설정
        mGoalEdtText = (EditText)findViewById(R.id.edittext_goal);

        mDayGoalEdtText = (EditText)findViewById(R.id.edittext_daygoal);       // 일목표
        mOnceEdtText = (EditText)findViewById(R.id.edittext_once);             // 일 회 수행 양(?)
        mSpinnerUnit = (Spinner)findViewById(R.id.spinner_unit);               // 단위
        mUnitTextview = (TextView)findViewById(R.id.textview_unit);

        // 요일 토글 버튼 id array
        mDayofWeekToggleBtnId = new int[7];
        mDayofWeekToggleBtnId[0] = R.id.togglebtn_sunday;
        mDayofWeekToggleBtnId[1] = R.id.togglebtn_monday;
        mDayofWeekToggleBtnId[2] = R.id.togglebtn_tuesday;
        mDayofWeekToggleBtnId[3] = R.id.togglebtn_wednesday;
        mDayofWeekToggleBtnId[4] = R.id.togglebtn_thursday;
        mDayofWeekToggleBtnId[5] = R.id.togglebtn_friday;
        mDayofWeekToggleBtnId[6] = R.id.togglebtn_saturday;

        // 요일 버튼
        mDayofWeekToggleBtn = new ToggleButton[7];
        mDayofWeekToggleBtn[0] = (ToggleButton)findViewById(R.id.togglebtn_sunday);
        mDayofWeekToggleBtn[1] = (ToggleButton)findViewById(R.id.togglebtn_monday);
        mDayofWeekToggleBtn[2] = (ToggleButton)findViewById(R.id.togglebtn_tuesday);
        mDayofWeekToggleBtn[3] = (ToggleButton)findViewById(R.id.togglebtn_wednesday);
        mDayofWeekToggleBtn[4] = (ToggleButton)findViewById(R.id.togglebtn_thursday);
        mDayofWeekToggleBtn[5] = (ToggleButton)findViewById(R.id.togglebtn_friday);
        mDayofWeekToggleBtn[6] = (ToggleButton)findViewById(R.id.togglebtn_saturday);

        mEveryDayToggleBtn = (ToggleButton)findViewById(R.id.togglebtn_everyday);
        mEveryWeekToggleBtn = (ToggleButton)findViewById(R.id.togglebtn_everyweek);

        // 시간 아침 오후 저녁toggleBtn_morning
        mMorningTimeToggleBtn   = (ToggleButton)findViewById(R.id.toggleBtn_morning);     // 아침 버튼
        mAfternoonTimeToggleBtn = (ToggleButton)findViewById(R.id.toggleBtn_afternoon);   // 오후 버튼
        mNightTimeToggleBtn     = (ToggleButton)findViewById(R.id.toggleBtn_night);       // 저녁 버튼
        mAllTimeToggleBtn       = (ToggleButton)findViewById(R.id.toggleBtn_all);       // 저녁 버튼

        mHabitFactory = new HabitMaker();

    }


    /**
     *  취미 선택 시 컴포넌트 set
     * @param habit
     */
    private void setComponent(UserHabitDetail habit) {
        if(habit == null)
            return;

        mHabitType.setText(habit.getName());

        // 목표 set
        mGoalEdtText.setText(habit.getGoal());

        // 1일 목표
        mDayGoalEdtText.setText(Integer.toString(habit.getFull()));
        mOnceEdtText.setText(Integer.toString(habit.getOnce()));

        //


        connectDB();
        List<String> unitList = mUserHabitRespository.getHabitUnit(habit.getHabitcode());
        disconnectDB();
//        Log.d(TAG, "get UnitList ?? "+ Integer.toString(unitList.size()));

        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, unitList );
        mSpinnerUnit.setAdapter(arrayAdapter);
        mSpinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHabit.setUnit((String) mSpinnerUnit.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerUnit.setId(0);
        mHabit.setUnit(unitList.get(0));
        mUnitTextview.setText(unitList.get(0));


        // 시간SET
         if(habit.getTime() == Habit.MORNING_TIME){
             mMorningTimeToggleBtn.setChecked(true);
             mAfternoonTimeToggleBtn.setChecked(false);
             mNightTimeToggleBtn.setChecked(false);
             mAllTimeToggleBtn.setChecked(false);
         }else if(habit.getTime() == Habit.AFTERNOON_TIME){
             mMorningTimeToggleBtn.setChecked(false);
             mAfternoonTimeToggleBtn.setChecked(true);
             mNightTimeToggleBtn.setChecked(false);
             mAllTimeToggleBtn.setChecked(false);
         }else if(habit.getTime() == Habit.NIGHT_TIME){
             mMorningTimeToggleBtn.setChecked(false);
             mAfternoonTimeToggleBtn.setChecked(false);
             mNightTimeToggleBtn.setChecked(true);
             mAllTimeToggleBtn.setChecked(false);
         }else if(habit.getTime() == Habit.ALLDAY_TIME) {
             // TODO 하루종일
             mMorningTimeToggleBtn.setChecked(false);
             mAfternoonTimeToggleBtn.setChecked(false);
             mNightTimeToggleBtn.setChecked(false);
             mAllTimeToggleBtn.setChecked(true);
         }else{
             mMorningTimeToggleBtn.setChecked(false);
             mAfternoonTimeToggleBtn.setChecked(false);
             mNightTimeToggleBtn.setChecked(false);
             mAllTimeToggleBtn.setChecked(false);
         }

        Log.d(TAG, "what time??? "+ mHabit.getTime());
        setDayofWeekToggle(habit);

    }

    /**
     *  취미 요일 set
     * @param habit
     */
    private void setDayofWeekToggle(UserHabitDetail habit) {

        if(habit == null){
            return;
        }
        int tmpDaySum = habit.getDaysum();

        for(int i = 1 ; i < 8 ; i ++){
            if((tmpDaySum & ( 1<< i) ) > 0){
                mDayofWeekToggleBtn[i-1].setChecked(true);
            }
        }

        Log.d(TAG, "DayofWeek "+ Integer.toString(tmpDaySum));
        // 메일
        if(tmpDaySum== 254) {
            mEveryDayToggleBtn.setChecked(true);
            mEveryWeekToggleBtn.setChecked(false);
        }
        else {
            mEveryDayToggleBtn.setChecked(false);
            mEveryWeekToggleBtn.setChecked(true);
        }
    }

    /**
     *
     * @param v
     */
    // 요일 버튼 클릭
    public void onClickDayofWeek(View v){
        // 습관 미선택시 안됨
        if(mHabit == null){
            return;
        }

        switch(v.getId()){
            case R.id.togglebtn_sunday :
                mHabit.setDaysumUsingOf(Calendar.SUNDAY, mDayofWeekToggleBtn[0].isChecked());
                break;
            case R.id.togglebtn_monday :
                mHabit.setDaysumUsingOf(Calendar.MONDAY, mDayofWeekToggleBtn[1].isChecked());
                break;
            case R.id.togglebtn_tuesday :
                mHabit.setDaysumUsingOf(Calendar.TUESDAY, mDayofWeekToggleBtn[2].isChecked());
                break;
            case R.id.togglebtn_wednesday :
                mHabit.setDaysumUsingOf(Calendar.WEDNESDAY, mDayofWeekToggleBtn[3].isChecked());
                break;
            case R.id.togglebtn_thursday :
                mHabit.setDaysumUsingOf(Calendar.THURSDAY, mDayofWeekToggleBtn[4].isChecked());
                break;
            case R.id.togglebtn_friday :
                mHabit.setDaysumUsingOf(Calendar.FRIDAY, mDayofWeekToggleBtn[5].isChecked());
                break;
            case R.id.togglebtn_saturday :
                mHabit.setDaysumUsingOf(Calendar.SATURDAY, mDayofWeekToggleBtn[6].isChecked());
                break;
            default :
                break;

        }
/*
        if (mHabit.getDaysum() != 254) {
            mEveryDayToggleBtn.setChecked(false);
            mEveryWeekToggleBtn.setChecked(true);
        }else {
            mEveryDayToggleBtn.setChecked(true);
            mEveryWeekToggleBtn.setChecked(false);
        }*/

Log.d(TAG, "DaySUM "+ Integer.toString(mHabit.getDaysum()));
        if (mHabit.getDaysum() == 254) {
            Log.d(TAG, "DaySUM 254 "+ Integer.toString(mHabit.getDaysum()));
            mEveryDayToggleBtn.setChecked(true);
            mEveryWeekToggleBtn.setChecked(false);
        } else if (mHabit.getDaysum() == 0){
            Log.d(TAG, "DaySUM 0 "+ Integer.toString(mHabit.getDaysum()));
            mEveryDayToggleBtn.setChecked(false);
            mEveryWeekToggleBtn.setChecked(false);
        }else{
            Log.d(TAG, "DaySUM 254 "+ Integer.toString(mHabit.getDaysum()));
            mEveryDayToggleBtn.setChecked(false);
            mEveryWeekToggleBtn.setChecked(true);
        }

        Log.d(TAG, "Every dayday" +Integer.toString(mHabit.getDaysum()));
    }

    /**
     * 빈도 매일선택
     *  일 월 화 수 목 금 토 set
     * @param v
     */
    public void onClickEveryDay(View v){
        // 습관 미선택시 안됨
        if(mHabit == null){
            return;
        }

        boolean isEveryDay = mEveryDayToggleBtn.isChecked();
        mEveryWeekToggleBtn.setChecked(!isEveryDay);

        if(isEveryDay){
            Log.d(TAG, "Everyday true");
        }else{
            Log.d(TAG, "Everyday false");
        }

        // 요일 버튼 set
        for(int i = 1 ; i < 8 ; i ++){
            ToggleButton tmpDayofWeekToggleBtn = mDayofWeekToggleBtn[i-1];
            tmpDayofWeekToggleBtn.setChecked(isEveryDay);
        }

        // true 이면 매일 아니면 오늘
        if(isEveryDay) {
            // habit 클래스 set
            mHabit.setDaysum(254);
        }else{
            Calendar calendar = Calendar.getInstance();
            int tmpDayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
            mDayofWeekToggleBtn[tmpDayofWeek].setChecked(true);
            mHabit.setDaysum(0);
            mHabit.setDaysumUsingOf(tmpDayofWeek, true);
        }
        Log.d(TAG, "Every Day " + Integer.toString(mHabit.getDaysum()));
    }

    /**
     * 빈도 매주선택
     *  오늘 날짜 Set
     * @param v
     */    public void onClickEveryWeek(View v){
        // 습관 미선택시 안됨
        if(mHabit == null){
            return;
        }

        boolean isEveryWeek = mEveryWeekToggleBtn.isChecked();
        mEveryDayToggleBtn.setChecked(!isEveryWeek);

        if(isEveryWeek){
            Log.d(TAG, "isEveryWeek true");
        }else{
            Log.d(TAG, "isEveryWeek false");
        }

        // 요일 버튼 set
        for(int i = 1 ; i < 8 ; i ++){
            ToggleButton tmpDayofWeekToggleBtn = mDayofWeekToggleBtn[i-1];
            tmpDayofWeekToggleBtn.setChecked(!isEveryWeek);
        }
        // habit 클래스 set

        if(isEveryWeek){
            // 오늘만 활성화
            Calendar calendar = Calendar.getInstance();
            int tmpDayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
            mDayofWeekToggleBtn[tmpDayofWeek-1].setChecked(true);
            mHabit.setDaysum(0);
            mHabit.setDaysumUsingOf(tmpDayofWeek, true);

        }else{
            mHabit.setDaysum(254);
        }

        Log.d(TAG, "Every Week " + Integer.toString(mHabit.getDaysum()));
    }


    public void onClickTime(View v){

        switch (v.getId()){
            case R.id.toggleBtn_all:
                mMorningTimeToggleBtn.setChecked(false);
                mAfternoonTimeToggleBtn.setChecked(false);
                mNightTimeToggleBtn.setChecked(false);
                mAllTimeToggleBtn.setChecked(true);
                mHabit.setTime(Habit.ALLDAY_TIME);
                break;
            case R.id.toggleBtn_morning :
                mMorningTimeToggleBtn.setChecked(true);
                mAfternoonTimeToggleBtn.setChecked(false);
                mNightTimeToggleBtn.setChecked(false);
                mAllTimeToggleBtn.setChecked(false);
                mHabit.setTime(Habit.MORNING_TIME);
                break;
            case R.id.toggleBtn_afternoon :
                mMorningTimeToggleBtn.setChecked(false);
                mAfternoonTimeToggleBtn.setChecked(true);
                mNightTimeToggleBtn.setChecked(false);
                mAllTimeToggleBtn.setChecked(false);
                mHabit.setTime(Habit.AFTERNOON_TIME);
                break;
            case R.id.toggleBtn_night :
                mMorningTimeToggleBtn.setChecked(false);
                mAfternoonTimeToggleBtn.setChecked(false);
                mNightTimeToggleBtn.setChecked(true);
                mAllTimeToggleBtn.setChecked(false);
                mHabit.setTime(Habit.NIGHT_TIME);
                break;
            default :
                mMorningTimeToggleBtn.setChecked(false);
                mAfternoonTimeToggleBtn.setChecked(false);
                mNightTimeToggleBtn.setChecked(false);
                mAllTimeToggleBtn.setChecked(false);
                mHabit.setTime(-1); // 마지막에 -1이면 안되게 하기
                break;
        }

        Log.d(TAG, "what time??? "+ mHabit.getTime());

    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            mHabitTimeRecyclerViewAdapter.setHabitTime(hourOfDay+":"+minute);
        }
    };

    public void onClickSaveHabit(View v){
        if(mHabit == null){
            return;
        }

        // 타이틀 확인
        if(mGoalEdtText.getText().toString().length() < 1){
            Toast.makeText(this, "목표가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        mHabit.setGoal(mGoalEdtText.getText().toString());

        // 하루목표 확인
        if(mDayGoalEdtText.getText().toString().length() < 1){
            Toast.makeText(this, "일 목표가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        mHabit.setFull(Integer.parseInt(mDayGoalEdtText.getText().toString()));

        // 하루 수행 확인
        if(mOnceEdtText.getText().toString().length() < 1){
            Toast.makeText(this, "1회 수행 량이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        mHabit.setOnce(Integer.parseInt(mOnceEdtText.getText().toString()));

        // 요일 확인
        if(mHabit.getDaysum() < 1){
            Toast.makeText(this, "요일이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO 시간은 여기서 버튼 눌렀는지 확인
        // 버튼 누른거로 다시 SET 해야겟다.
        Toast.makeText(this, "getTime "+Integer.toString(mHabit.getTime()), Toast.LENGTH_SHORT).show();
        if(mHabit.getTime() < 0 ){
            Toast.makeText(this, "시간이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mHabit.getUnit().length()<1){
            Toast.makeText(this, "단위 선택이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        connectDB();

        if(mHabit.getHabitseq() == 0 ) {
            Log.d(TAG, "DB TEST 날짜 확인 :  " +mHabit.getDaysum() + " 시간 확인 : "+mHabit.getTime() +" 단위 확인 : "+mHabit.getUnit());
            saveHait();
        }else{
            updateHait();
        }
        disconnectDB();
    }

    private void saveHait() {
        int detailSeq = mUserHabitRespository.getMaxSeqHabitDetail();
//        int priority  = mUserHabitRespository.getMaxPriorityHabitDetail(mHabit.getTime());
        int stateSeq  = mUserHabitRespository.getMaxSeqUserHabitState();

        mHabit.setHabitseq(detailSeq+1);
//        mHabit.setPriority(priority+1);

        Toast.makeText(this, "DB TEST get Seq "+Integer.toString(detailSeq) + " / "+ Integer.toString(stateSeq) , Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "DB TEST get Seq "+Integer.toString(detailSeq) + " / "+ Integer.toString(stateSeq) +"/"+  Integer.toString(priority), Toast.LENGTH_SHORT).show();


        List<UserHabitState> userHabitStateList  = new ArrayList<>();
        int userStatepriority =0;

        int userStateSeq = mUserHabitRespository.getMaxSeqUserHabitState();
        Log.d(TAG, "DB TEST time "+mHabit.getTime()  );

        Log.d(TAG, "DB TEST "+userStatepriority+1 +"  "+userStateSeq+1);
        // user state 습관 넣기

        for (int dayofweek = 1; dayofweek < 8; dayofweek++) {
            if ((mHabit.getDaysum() & (1 << dayofweek)) > 0) {
                //userStatepriority = mUserHabitRespository.getMaxPriorityUserHabitState(mHabit.getTime(), dayofweek);
                Log.d(TAG, "DB TEST userstatepri "+userStatepriority);
                userStatepriority++;
                userStateSeq++;
                Log.d(TAG, "DB TEST userstatepri "+userStatepriority);
//                UserHabitState tmpState = new UserHabitState(userStateSeq,userStatepriority, dayofweek, mHabit);
                UserHabitState tmpState = new UserHabitState(userStateSeq, dayofweek, mHabit);
                userHabitStateList.add(tmpState);
//                Log.d(TAG,  "DB TEST  make state "+tmpState.getDayofweek() +"/"+tmpState.getPriority()+"/"+tmpState.getDaysum()+"/"+tmpState.getTime() +"/"+ tmpState.getMasterseq()  +"/"+ tmpState.getHabitcode() +"/"+  tmpState.getName() +"/"+ tmpState.getGoal() +"/"+ tmpState.getDaysum() +"/"+ tmpState.getFull() +"/"+ tmpState.getUnit() );
                Log.d(TAG,  "DB TEST  make state "+tmpState.getDayofweek() +"/"+tmpState.getDaysum()+"/"+tmpState.getTime() +"/"+ tmpState.getMasterseq()  +"/"+ tmpState.getHabitcode() +"/"+  tmpState.getName() +"/"+ tmpState.getGoal() +"/"+ tmpState.getDaysum() +"/"+ tmpState.getFull() +"/"+ tmpState.getUnit() );
            }

        }

        mUserHabitRespository.insertUserHabit(mHabit , userHabitStateList);
    }

    //TODO 업데이트 안했다~
    private void updateHait() {
        // transaction 으로 detail 과state를 같이 해야한다.


        // 2. 찾아온 UserHabitState와 비교하면서 요일별로 확인 하며 담는다.
        //    - update 있으면 값 변경
        //    - insert 없으면 새로 만들어준다.
        int stateseq = mUserHabitRespository.getMaxSeqUserHabitState();
        List<UserHabitState> insertHabitStateList = new ArrayList<>();
        int daySum = mHabit.getDaysum();
        for(int dayofweek = 1 ; dayofweek < 8 ; dayofweek ++){
            if ((mHabit.getDaysum() & (1 << dayofweek)) > 0) {
                stateseq++;
                insertHabitStateList.add(new UserHabitState(stateseq, dayofweek, mHabit));
            }
        }

        // Detail은 UPDATE
        // State는 DELETE INSERT
        mUserHabitRespository.updateUserHabit(mHabit, insertHabitStateList);
        // 4. UserHabitDetail update
        //    UserHabitState - delete
        //                   - update
        //                   - insert
        //    그냥 지우고 새로 넣는거는 어떨까?
        // 키가 되는 값

    }

    private void connectDB(){

        mUserHabitRespository = new UserHabitRespository(getApplication());
    }


    private void disconnectDB(){

        mUserHabitRespository.destroyInstance();
    }


}
