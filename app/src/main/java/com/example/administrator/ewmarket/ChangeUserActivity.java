package com.example.administrator.ewmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.SpUtils;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class ChangeUserActivity extends Activity implements View.OnClickListener
{
    private ImageView bt_login_back;
    //登录用户名的那个输入框
    private EditText login_id;
    //登录输入用户密码的输入框
    private EditText login_password;
    //登录按钮
    private Button login_submit;
    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeuser);
        initView();
    }
    private void initView()
    {
        bt_login_back= (ImageView) this.findViewById(R.id.bt_login_back);
        login_id=(EditText)this.findViewById(R.id.login_id);
        login_password=(EditText)this.findViewById(R.id.login_password);
        login_submit=(Button) this.findViewById(R.id.login_submit);
        bt_login_back.setOnClickListener(this);
        login_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if (view==bt_login_back)
        {
            finish();
        }else if (view==login_submit)
        {
            submit();
        }
    }

    private void submit()
    {
        if (TextUtils.isEmpty(login_id.getText().toString())||TextUtils.isEmpty(login_password.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "对不起你输入的用户名或密码有误", Toast.LENGTH_SHORT).show();
        }
        //这时候密码和用户名都已经输入了
        else
        {
                        currentUser=SpUtils.getString(getApplicationContext(),"currentUser","");
                        if (login_id.getText().toString().equals(currentUser))
                        {
                            Toast.makeText(getApplicationContext(), "该用户已经登录", Toast.LENGTH_SHORT).show();
                        }else { 

            final UserInformationDao userInformationDao=UserInformationDao.getInstance(this);
            if (userInformationDao.find(login_id.getText().toString()))
            {
                if (userInformationDao.checkUser(login_id.getText().toString(),login_password.getText().toString()))
                {
                    // Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("提示信息");
                    builder.setMessage("恭喜你登录成功");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {   //记录当前登录的用户
                            SpUtils.putString(getApplicationContext(),"currentUser",login_id.getText().toString());
                            //记录用户登录的用户名及密码，先这么干以后看看MD5加密
                            SpUtils.putString(getApplicationContext(),"user:"+login_id.getText().toString(),"password:"+login_password.getText().toString());
                            Toast.makeText(getApplicationContext(), login_id.getText().toString()+"登入成功", Toast.LENGTH_SHORT).show();
                            //发送自定义的广播
                            Intent intent=new Intent();
                            intent.setAction(ConstantValues.LoginSuccess);
                            sendBroadcast(intent);
                            ChangeUserActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            Toast.makeText(getApplicationContext(), login_id.getText().toString()+"登入成功", Toast.LENGTH_SHORT).show();
                            ChangeUserActivity.this.finish();
                        }
                    });
                    builder.create().show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "对不起你输入密码错误", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                Toast.makeText(getApplicationContext(), "对不起该用户不存在", Toast.LENGTH_SHORT).show();
            }
        }

        }
    }
}
