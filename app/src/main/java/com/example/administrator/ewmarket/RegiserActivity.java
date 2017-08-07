package com.example.administrator.ewmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.UIUtils;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class RegiserActivity extends Activity implements View.OnClickListener
{
    private ImageView bt_login_back;
    //注册用户名的那个输入框
    private EditText login_id;
    //注册输入用户密码的输入框
    private EditText login_password;
    //注册按钮
    private Button login_register;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser);
        initView();
    }

    private void initView()
    {

        login_id=(EditText)this.findViewById(R.id.login_id);
        login_password=(EditText)this.findViewById(R.id.login_password);
        login_register=(Button) this.findViewById(R.id.login_register);
        bt_login_back= (ImageView) this.findViewById(R.id.bt_login_back);

        bt_login_back.setOnClickListener(this);
        login_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view==bt_login_back)
        {
               finish();
        }
        else if (view==login_register)
        {
            if (TextUtils.isEmpty(login_id.getText().toString())||TextUtils.isEmpty(login_password.getText().toString()))
            {
               Toast.makeText(getApplicationContext(),"对不起你输入的用户名或密码有误",Toast.LENGTH_SHORT).show();
           }else
           {
               final UserInformationDao userInformationDao=UserInformationDao.getInstance(this);
               //如果在数据库中能查到该用户名，就弹出Toast告诉注册用户改用户名已经被注册
               if (userInformationDao.find(login_id.getText().toString().trim()))
               {
                   Toast.makeText(getApplicationContext(),"对不起该用户名已经被注册",Toast.LENGTH_SHORT).show();
               }else //这时候代表注册成功对吧
               {

                   AlertDialog.Builder Builder=new AlertDialog.Builder(this);
                   Builder.setTitle("提示信息");
                   Builder.setMessage("恭喜你当前用户可以注册，是否确定注册");
                   Builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i)
                       {
                      /*     //点击确定之后，把用户信息写到数据库  R.mipmap.userlufei,  R.mipmap.usershanzhi,分别为路飞与山治的头像，已经注册完了.这个R.mipmap.ic_person_center是默认头像
                           if (login_id.getText().toString().trim().equals("路飞"))
                           {
                               userInformationDao.addUser(login_id.getText().toString().trim(), login_password.getText().toString(), R.mipmap.userlufei);
                           }
                           else if(login_id.getText().toString().trim().equals("香吉士"))
                           {
                               userInformationDao.addUser(login_id.getText().toString().trim(), login_password.getText().toString(),R.mipmap.usershanzhi);
                           }
                           //超级用户娜美
                           else if(login_id.getText().toString().trim().equals("娜美"))
                           {
                               userInformationDao.addUser(login_id.getText().toString().trim(), login_password.getText().toString(),R.mipmap.user_namei);
                           }
                           else {
                               userInformationDao.addUser(login_id.getText().toString().trim(), login_password.getText().toString(), R.mipmap.ic_person_center);
                           }

                           Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                           RegiserActivity.this.finish();*/
                           Bundle bundle=new Bundle();
                           bundle.putString("username",login_id.getText().toString().trim());
                           bundle.putString("password", login_password.getText().toString());
                           Env.startActivity(getApplicationContext(),SelectedUserIconActivity.class,bundle);
                       }
                   });
                   Builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i)
                       {
                         //点击取消这块什么也不做就行，对话框会自动消失
                       }
                   });
                   Builder.create().show();
               }

           }


        }

    }
}
