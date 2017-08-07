package com.example.administrator.ewmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.DestinationFragment;
import com.example.administrator.ewmarket.app.FinderFragment;
import com.example.administrator.ewmarket.app.HomeFragment;
import com.example.administrator.ewmarket.app.MyFragment;
import com.example.administrator.ewmarket.app.TripFragment;
import com.example.administrator.ewmarket.app.View.RoundImageView;
import com.example.administrator.ewmarket.app.View.SlideMenu;
import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.MyBroadcastListener;
import com.example.administrator.ewmarket.app.utils.SpUtils;
import com.example.administrator.ewmarket.app.utils.UIUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public final static String tag = "MainActivity";
    private MarketTopBar mMarketTopBar;
    private MarketBottomBar mMarketBottomBar;
    private FrameLayout mFrgContainer;
    private HomeFragment mhomeFragment;
    private DestinationFragment mdestinationFragment;
    private FinderFragment mfinderFragment;
    private TripFragment mtripFragment;
    private MyFragment mmyFragment;
    private Fragment lastFragment;
    private SlideMenu slideMenu;
    private RoundImageView iv_user_center;
    private RoundImageView slidemenu_user_icon;
    private String currentUser;
    private int currentUserIcon;
    private TextView slidemenu_user_name;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private String mSdFileAbsolutePath = UIUtils.getContext().getExternalFilesDir(null).getAbsolutePath();
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private MyBroadcastListener.OnLoginSuccessListener myLoginSuccessListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ((EWApp) getApplicationContext()).getMyBroadcastListener().removeLoginSuccessListener(myLoginSuccessListener);
    }

    private void initViews()
    {
        mMarketTopBar = (MarketTopBar) this.findViewById(R.id.market_topbar);
        mMarketBottomBar = (MarketBottomBar) this.findViewById(R.id.market_bottombar);
        mFrgContainer = (FrameLayout) this.findViewById(R.id.realtabcontent);
        iv_user_center = (RoundImageView) mMarketTopBar.findViewById(R.id.iv_user_center);

        slideMenu = (SlideMenu) this.findViewById(R.id.sm_slidemenu);
        slidemenu_user_icon=(RoundImageView)slideMenu.findViewById(R.id.slidemenu_user_icon);
        slidemenu_user_name=(TextView)slideMenu.findViewById(R.id.slidemenu_user_name);

        currentUser = SpUtils.getString(getApplicationContext(), ConstantValues.CURRENT_USER, "");
        currentUserIcon = SpUtils.getInt(getApplicationContext(), ConstantValues.CURRENT_USERICON, R.mipmap.ic_person_center);
        slidemenu_user_name.setText(currentUser);
        slidemenu_user_icon.setImageResource(currentUserIcon);

        slidemenu_user_icon.setOnClickListener(this);
        iv_user_center.setOnClickListener(this);
        //判断已经登入的用户，省了下次进app还里从新登录才能获取数据
        if (currentUser.equals(""))
        {
            //Toast.makeText(getApplicationContext(),"当前app目前还没有用户登入过",Toast.LENGTH_LONG).show();
        }
        else
        {
            int currentUserIcon = SpUtils.getInt(getApplicationContext(), ConstantValues.CURRENT_USERICON, R.mipmap.ic_person_center);
            //Toast.makeText(getApplicationContext(), Integer.toString(currentUserIcon), Toast.LENGTH_SHORT).show();
            //iv_user_center.setBackgroundResource(currentUserIcon);
            //iv_user_center.setImageResource(currentUserIcon);
            Bitmap bitmap = ImageUtils.intToBitmap(UIUtils.getContext(), currentUserIcon);
            if (bitmap == null)
            {
                //重sd卡中读取文件bitmap
                int icon = currentUserIcon;
                bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath + "/UserIconPicture" + "/" + String.valueOf(icon) + ".png");
            }

            if (bitmap != null)
            {

                iv_user_center.setImageBitmap(bitmap);
                slidemenu_user_icon.setImageBitmap(bitmap);
            }
            else
            {
                //如果在资源和sd卡内存中都没有找到就给设置一个默认的头像
                Bitmap bitmap1 = ImageUtils.intToBitmap(UIUtils.getContext(), R.mipmap.ic_person_center);
                iv_user_center.setImageBitmap(bitmap1);
                slidemenu_user_icon.setImageBitmap(bitmap1);
            }
        }

        //实现监听到的广播
        myLoginSuccessListener = new MyBroadcastListener.OnLoginSuccessListener()
        {
            @Override
            public void onUpdateUi()
            {
                //在sharepreference中拿到当前用户名
                String currentUser = SpUtils.getString(getApplicationContext(), "currentUser", "");
                //接下来就是数据库查找当前用户的icon来吧
                UserInformationDao userInformationDao = UserInformationDao.getInstance(getApplicationContext());
                int currentUserIcon = userInformationDao.findIcon(currentUser);
                // iv_user_center.setBackgroundResource(currentUserIcon);
                Bitmap bitmap = ImageUtils.intToBitmap(UIUtils.getContext(), currentUserIcon);
                if (bitmap == null)
                {
                    //重sd卡中读取文件bitmap
                    int icon = currentUserIcon;
                    bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath + "/UserIconPicture" + "/" + String.valueOf(icon) + ".png");
                }

                if (bitmap != null)
                {

                    iv_user_center.setImageBitmap(bitmap);
                    slidemenu_user_icon.setImageBitmap(bitmap);
                }
                else
                {
                    //如果在资源和sd卡内存中都没有找到就给设置一个默认的头像
                    Bitmap bitmap1 = ImageUtils.intToBitmap(UIUtils.getContext(), R.mipmap.ic_person_center);
                    iv_user_center.setImageBitmap(bitmap1);
                    slidemenu_user_icon.setImageBitmap(bitmap1);
                }
               // iv_user_center.setImageResource(currentUserIcon);
                //slidemenu_user_icon.setImageResource(currentUserIcon);
                slidemenu_user_name.setText(currentUser);
                //之后把那个当前用户的icon也放到sharedPrefenence里吧，方便后面直接用省了数据库在查是不
                SpUtils.putInt(getApplicationContext(), ConstantValues.CURRENT_USERICON, currentUserIcon);
            }
        };
        //把这个listener添加到数组中
        ((EWApp) getApplicationContext()).getMyBroadcastListener().addLoginSuccessListener(myLoginSuccessListener);

    }
    @Override
    public void onClick(View view)
    {
        if (view==slidemenu_user_icon)
        {
            Env.startActivity(MainActivity.this,LoginActivity.class,null);
        }else if (view==iv_user_center)
        {
            slideMenu.switchState();
        }
    }

    public synchronized void initFragment(int page)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //这块要是用ft.replace这个方法就会每次切换fragment时生命周期都重走一遍，所以使用ft.add比较好,这么写不知道对不对
        switch (page)
        {
            case 0:

                if (mhomeFragment == null)
                {
                    mhomeFragment = new HomeFragment();
                }

                if (!mhomeFragment.isAdded())
                {
                    if (lastFragment != null)
                    {   //如果不加这句如果点击切换太快，上个fragment可能直接显示在当前的fragment上
                        ft.hide(lastFragment);
                    }

                    ft.add(R.id.realtabcontent, mhomeFragment);
                    ft.commit();
                    lastFragment = mhomeFragment;
                }
                else
                {
                    ft.hide(lastFragment);
                    ft.show(mhomeFragment);
                    ft.commit();
                    lastFragment = mhomeFragment;
                }

                break;
            case 1:
                if (mdestinationFragment == null)
                {
                    mdestinationFragment = new DestinationFragment();
                }

                if (!mdestinationFragment.isAdded())
                {
                    if (lastFragment != null)
                    {
                        ft.hide(lastFragment);
                    }
                    ft.add(R.id.realtabcontent, mdestinationFragment);
                    ft.commit();
                    lastFragment = mdestinationFragment;
                }
                else
                {
                    ft.hide(lastFragment);
                    ft.show(mdestinationFragment);
                    ft.commit();
                    lastFragment = mdestinationFragment;
                }
                break;
            case 2:
                if (mfinderFragment == null)
                {
                    mfinderFragment = new FinderFragment();

                }
                if (!mfinderFragment.isAdded())
                {
                    if (lastFragment != null)
                    {
                        ft.hide(lastFragment);
                    }
                    ft.add(R.id.realtabcontent, mfinderFragment);
                    ft.commit();
                    lastFragment = mfinderFragment;
                }
                else
                {
                    ft.hide(lastFragment);
                    ft.show(mfinderFragment);
                    ft.commit();
                    lastFragment = mfinderFragment;
                }
                break;
            case 3:
                if (mtripFragment == null)
                {
                    mtripFragment = new TripFragment();
                }

                if (!mtripFragment.isAdded())
                {
                    if (lastFragment != null)
                    {
                        ft.hide(lastFragment);
                    }
                    ft.add(R.id.realtabcontent, mtripFragment);
                    ft.commit();
                    lastFragment = mtripFragment;
                }
                else
                {
                    ft.hide(lastFragment);
                    ft.show(mtripFragment);
                    ft.commit();
                    lastFragment = mtripFragment;
                }
                break;
            case 4:
                if (mmyFragment == null)
                {
                    mmyFragment = new MyFragment();
                }

                if (!mmyFragment.isAdded())
                {
                    if (lastFragment != null)
                    {
                        ft.hide(lastFragment);
                    }
                    ft.add(R.id.realtabcontent, mmyFragment);
                    ft.commit();
                    lastFragment = mmyFragment;
                }
                else
                {
                    ft.hide(lastFragment);
                    ft.show(mmyFragment);
                    ft.commit();
                    lastFragment = mmyFragment;
                }
                break;
            default:
                break;

        }
    }

    //z暂时这么写以后再优化
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(1, 1, 1, "版本更新");
        menu.add(1, 2, 2, "反馈");
        menu.add(1, 3, 3, "退出");
        menu.add(2, 4, 4, "取消");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case 1://"版本更新"
                break;
            case 2://"反馈"
                break;
            case 3://"退出"
                this.finish();
                break;
            case 4://"取消"
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit()
    {
        if (!isExit)
        {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
        else
        {
            finish();
            System.exit(0);
        }
    }


}
