package com.example.administrator.ewmarket;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.DateTimePickDialogUtil;
import com.example.administrator.ewmarket.app.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class EditInfoActivity extends Activity implements View.OnClickListener
{
    private TextView tv_back;
    //名字
    private EditText et_name;
    //RadioGroup组
    private RadioGroup ll_gender;
    //男
    private RadioButton rb_male;
    //女
    private RadioButton rb_female;
    //出生日期
    private EditText et_birthday;
    //英文名字
    private EditText tv_english_name;
    //电话号码
    private EditText et_phone_number;
    //保存个人信息资料按钮
    private TextView tv_save_button;
    private String initDateTime = "1990年1月1日"; // 设置对话框弹出的初始时间
    //这些布尔值现在不设置默认值表示空值，不解，以前的不设置就代表false。。。
    private Boolean ismale=false;
    private Boolean isfemale=false;
    private String userCurrent;
    private UserInformationDao userInformationDao;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tourist_user_center);
        initView();
        initData();
    }

    private void initView()
    {
        tv_back=(TextView) findViewById(R.id.tv_back);
        et_name=(EditText)findViewById(R.id.et_name);
        ll_gender=(RadioGroup)findViewById(R.id.ll_gender);
        rb_male=(RadioButton)findViewById(R.id.rb_male);
        rb_female=(RadioButton)findViewById(R.id.rb_female);
        et_birthday=(EditText)findViewById(R.id.et_birthday);
        tv_english_name=(EditText)findViewById(R.id.tv_english_name);
        et_phone_number=(EditText)findViewById(R.id.et_phone_number);
        tv_save_button=(TextView)findViewById(R.id.tv_save_button);


        tv_back.setOnClickListener(this);
        et_birthday.setOnClickListener(this);
        tv_save_button.setOnClickListener(this);
        ll_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (i)
                {
                    case R.id.rb_male:
                        ismale=true;
                        isfemale=false;
                        break;
                    case R.id.rb_female:
                        isfemale=true;
                        ismale=false;
                        break;
                }
            }
        });
    }
    private void initData()
    {
        userCurrent= SpUtils.getString(getApplicationContext(), ConstantValues.CURRENT_USER,"");
        userInformationDao=UserInformationDao.getInstance(getApplicationContext());
        new Thread(){
           @Override
           public void run()
           {
               super.run();
               //进入该activity的时候应该把数据库中的个人信息显示在界面上
               final ArrayList<String>  userBaseList = (ArrayList<String>) userInformationDao.queryUserInfo(userCurrent);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run()
                   {
                       et_name.setText(userBaseList.get(0));
                       if (userBaseList.get(1).equals("男"))
                       {
                           ll_gender.check(R.id.rb_male);
                       }else if(userBaseList.get(1).equals("女")) {
                           ll_gender.check(R.id.rb_female);
                       }else {
                           //这个时候是刚注册的用户第一次进入编辑界面时候，查询出来的userBaseList.get(1).equals("")什么也不做就行了
                       }
                       et_birthday.setText(userBaseList.get(2));
                       tv_english_name.setText(userBaseList.get(3));
                       et_phone_number.setText(userBaseList.get(4));
                   }
               });

           }
       }.start();
    }




    @Override
    public void onClick(View view)
    {
        if (view==tv_back)
        {
            finish();
        }
        if (view==et_birthday)
        {
            DateTimePickDialogUtil dateTimePicKDialog=new DateTimePickDialogUtil(
                    this, initDateTime);
            dateTimePicKDialog.dateTimePicKDialog(et_birthday);
        }
        if (view==tv_save_button)
        {
            upDataLibrary();
        }
    }

    private void upDataLibrary()
    {
        final String name=et_name.getText().toString().trim();
        final String birthday=et_birthday.getText().toString().trim();
        final String englishName=tv_english_name.getText().toString().trim();
        final String phoneNumber=et_phone_number.getText().toString().trim();


        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(birthday)&&!TextUtils.isEmpty(englishName)
                &&!TextUtils.isEmpty(phoneNumber)&&(ismale||isfemale))
        {
            //执行数据库的更新操作
           // Toast.makeText(getApplicationContext(),"更新数据库",Toast.LENGTH_SHORT).show();
            if (ismale==true)
            {
                final String sex="男";
                new Thread(){
                    @Override
                    public void run()
                    {
                        super.run();
                        userInformationDao.updateUserInfo(userCurrent,name,sex,birthday,englishName,phoneNumber);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(),"更新资料成功",Toast.LENGTH_SHORT).show();}
                        });
                    }
                }.start();
            }
            else
            {
             final String sex="女";
                new Thread(){
                    @Override
                    public void run()
                    {
                        super.run();
                        userInformationDao.updateUserInfo(userCurrent,name,sex,birthday,englishName,phoneNumber);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(),"更新资料成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            }
        }else {
            Toast.makeText(getApplicationContext(),"对不起请填写资料完整",Toast.LENGTH_SHORT).show();
        }
    }
}
