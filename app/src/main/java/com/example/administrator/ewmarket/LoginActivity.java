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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.SpUtils;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class LoginActivity extends Activity implements View.OnClickListener
{
    private ImageView bt_login_back;
    private TextView tv_changeuser;
    //登录用户名的那个输入框
    private EditText login_id;
    //登录输入用户密码的输入框
    private EditText login_password;
    //登录按钮
    private Button login_submit;
    //注册按钮
    private Button login_register;

    private LinearLayout layout_login_id;
    private RelativeLayout layout_password;
    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //这块是判断当前app是否已经有用户登录成功，如果用户登录成功，就把那几个控件设置为View.GONE
        currentUser=SpUtils.getString(getApplicationContext(),"currentUser","");
        if (!currentUser.equals(""))
        {
            layout_login_id.setVisibility(View.GONE);
            layout_password.setVisibility(View.GONE);
            login_submit.setVisibility(View.GONE);
        }

    }
    private void initView()
    {
        tv_changeuser=(TextView)this.findViewById(R.id.tv_changeuser);
        login_id=(EditText)this.findViewById(R.id.login_id);
        login_password=(EditText)this.findViewById(R.id.login_password);
        login_submit=(Button) this.findViewById(R.id.login_submit);
        login_register=(Button) this.findViewById(R.id.login_register);
        bt_login_back= (ImageView) this.findViewById(R.id.bt_login_back);
        layout_login_id=(LinearLayout)this.findViewById(R.id.layout_login_id);
        layout_password=(RelativeLayout)this.findViewById(R.id.layout_password);
//        bt_login_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                LoginActivity.this.finish();
//            }
//        });
        bt_login_back.setOnClickListener(this);
        tv_changeuser.setOnClickListener(this);
        login_submit.setOnClickListener(this);
        login_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if (view==bt_login_back)
        {
            LoginActivity.this.finish();
        }
        else if (view==tv_changeuser)
        {
             Env.startActivity(this,ChangeUserActivity.class,null);
        }
        else if (view==login_register)
        {
            Env.startActivity(this, RegiserActivity.class,null);
        }
        else if (view==login_submit)
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
//            currentUser=SpUtils.getString(getApplicationContext(),"currentUser","");
//            if (login_id.getText().toString().equals(currentUser))
//            {
//                Toast.makeText(getApplicationContext(), "该用户已经登录", Toast.LENGTH_SHORT).show();
//            }else {   }

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
                                SpUtils.putString(getApplicationContext(),ConstantValues.CURRENT_USER,login_id.getText().toString());
                                //记录用户登录的用户名及密码，先这么干以后看看MD5加密
                                SpUtils.putString(getApplicationContext(),"user:"+login_id.getText().toString(),"password:"+login_password.getText().toString());
                                Toast.makeText(getApplicationContext(), login_id.getText().toString()+"登入成功", Toast.LENGTH_SHORT).show();
                                //发送自定义的广播
                                Intent intent=new Intent();
                                intent.setAction(ConstantValues.LoginSuccess);
                                sendBroadcast(intent);
                                LoginActivity.this.finish();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Toast.makeText(getApplicationContext(), login_id.getText().toString()+"登入成功", Toast.LENGTH_SHORT).show();
                                LoginActivity.this.finish();
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
