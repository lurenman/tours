package com.example.administrator.ewmarket.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.DetailsActivity;
import com.example.administrator.ewmarket.DiscussActivity;
import com.example.administrator.ewmarket.EWApp;
import com.example.administrator.ewmarket.EditInfoActivity;
import com.example.administrator.ewmarket.LoginActivity;
import com.example.administrator.ewmarket.MainActivity;
import com.example.administrator.ewmarket.MyPublishActivity;
import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.engine.CurrentUserOrderGood;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.MemoryCacheUtils;
import com.example.administrator.ewmarket.app.utils.MyBroadcastListener;
import com.example.administrator.ewmarket.app.utils.SpUtils;
import com.example.administrator.ewmarket.app.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class MyFragment extends Fragment implements View.OnClickListener
{
    private View mLayoutView;
    //登录注册
    private Button bt_login_regiser;
    //个人资料
    private TextView tv_edit_info;
    //用户中心头像
    private ImageView iv_usericon;
    //超级用户娜美发布的textview，默认是隐藏
    private TextView tv_publish;
    private ListView lv_mlistview;
    private String currentUser;
    //当前用户订购的所有资讯集合
    private ArrayList<CurrentUserOrderGood> mOrderGoods;
    private TourInformationDao tourInformationDao;
    private UserInformationDao userInformationDao;
    //订购资讯集合的条目信息集合
    private ArrayList<TourInformation> TourInformations;
    private MemoryCacheUtils mMemoryCache;
    private final static int typeMyFragment = 0;
    private MyBaseAdapater mBaseAdapater;
    //自定义的广播
    private OrderGoodsBroadcastReceiver orderGoodsBroadcastReceiver;
    private String mSdFileAbsolutePath = UIUtils.getContext().getExternalFilesDir(null).getAbsolutePath();
    private PopupWindow mPopupWindow;
    private DeletePublishBroadcastReceiver deletePublishBroadcastReceiver;
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (TourInformations != null)
            {
                lv_mlistview.setAdapter(mBaseAdapater);
            }
        }
    };
    private Handler mUpdateHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (TourInformations != null)
            {
                if (mBaseAdapater != null)
                {
                    mBaseAdapater.notifyDataSetChanged();
                }
            }
        }
    };
    //通知更新ui的
    private MyBroadcastListener.OnLoginSuccessListener myLoginSuccessListener = new MyBroadcastListener.OnLoginSuccessListener()
    {
        @Override
        public void onUpdateUi()
        {
            //int currentUserIcon = SpUtils.getInt(getContext(), "currentUserIcon", R.mipmap.ic_person_center);
            //iv_usericon.setBackgroundResource(currentUserIcon);
            //iv_usericon.setImageResource(currentUserIcon);
            updateUserIcon();
            checkUser();
            updateData();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((EWApp) getContext().getApplicationContext()).getMyBroadcastListener().addLoginSuccessListener(myLoginSuccessListener);
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantValues.OrderGoodsSuccess);
        orderGoodsBroadcastReceiver = new OrderGoodsBroadcastReceiver();
        getContext().registerReceiver(orderGoodsBroadcastReceiver, intentFilter);
        //注册删除发布成功广播
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ConstantValues.DeletePublishSuccess);
        deletePublishBroadcastReceiver = new DeletePublishBroadcastReceiver();
        getContext().registerReceiver(deletePublishBroadcastReceiver, intentFilter1);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((EWApp) getContext().getApplicationContext()).getMyBroadcastListener().removeLoginSuccessListener(myLoginSuccessListener);
        if (orderGoodsBroadcastReceiver != null)
        {
            getContext().unregisterReceiver(orderGoodsBroadcastReceiver);
        }

    }

    @Override
    public void onClick(View view)
    {
        if (view == bt_login_regiser)
        {
            Env.startActivity(getContext(), LoginActivity.class, null);
        }
        else if (view == tv_edit_info)
        {       //小bug修复
            if (currentUser.equals(""))
            {
                Toast.makeText(getContext(), "对不起请先注册登录才可以编辑个人信息", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Env.startActivity(getContext(), EditInfoActivity.class, null);
            }
        }
        else if (view == tv_publish)
        {
            Env.startActivity(getContext(), MyPublishActivity.class, null);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mLayoutView == null)
        {
            mLayoutView = inflater.inflate(R.layout.fragment_app_my, null);
            mMemoryCache = new MemoryCacheUtils(typeMyFragment);
            initView();
            initData();
        }
        return mLayoutView;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView()
    {
        bt_login_regiser = (Button) mLayoutView.findViewById(R.id.bt_login_regiser);
        tv_edit_info = (TextView) mLayoutView.findViewById(R.id.tv_edit_info);
        tv_publish = (TextView) mLayoutView.findViewById(R.id.tv_publish);
        iv_usericon = (ImageView) mLayoutView.findViewById(R.id.iv_usericon);
        lv_mlistview = (ListView) mLayoutView.findViewById(R.id.lv_mlistview);
        mBaseAdapater = new MyBaseAdapater();
        bt_login_regiser.setOnClickListener(this);
        tv_edit_info.setOnClickListener(this);
        tv_publish.setOnClickListener(this);
        checkUser();

    }

    private void initData()
    {

        // iv_usericon.setBackgroundResource(currentUserIcon);
        //  iv_usericon.setImageResource(currentUserIcon);
        updateUserIcon();
        userInformationDao = UserInformationDao.getInstance(getContext());
        tourInformationDao = TourInformationDao.getInstance(getContext());
        findData();


    }
    private void updateUserIcon()
    {   int currentUserIcon = SpUtils.getInt(getContext(), "currentUserIcon", R.mipmap.ic_person_center);
        Bitmap bitmap = ImageUtils.intToBitmap(UIUtils.getContext(), currentUserIcon);
        if (bitmap == null)
        {
            //重sd卡中读取文件bitmap
            int icon = currentUserIcon;
            bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath + "/UserIconPicture" + "/" + String.valueOf(icon) + ".png");
        }

        if (bitmap != null)
        {
            iv_usericon.setImageBitmap(bitmap);
        }
        else
        {
            //如果在资源和sd卡内存中都没有找到就给设置一个默认的头像
            Bitmap bitmap1 = ImageUtils.intToBitmap(UIUtils.getContext(), R.mipmap.ic_person_center);
            iv_usericon.setImageBitmap(bitmap1);
        }
    }

    private void checkUser()
    {
        currentUser = SpUtils.getString(getContext(), ConstantValues.CURRENT_USER, "");
        if (currentUser.equals("娜美"))
        {
            tv_publish.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_publish.setVisibility(View.GONE);
        }
    }

    private void findData()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                queryData();
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void updateData()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                queryData();
                mUpdateHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void queryData()
    {
        //1.首先应该查到当前用户
        String currentUser = SpUtils.getString(getContext(), ConstantValues.CURRENT_USER, "");
        if (!currentUser.equals(""))
        {
            mOrderGoods = new ArrayList<CurrentUserOrderGood>();
            //2.在查用户所有订购的资讯
            mOrderGoods = (ArrayList<CurrentUserOrderGood>) userInformationDao.findCurrentUserAllOrderGoods(currentUser);
            //3.通过找到的订购的资讯tourname
            TourInformations = new ArrayList<TourInformation>();
            for (CurrentUserOrderGood mOrderGood : mOrderGoods)
            {
                String ordergood = mOrderGood.getOrdergoods();
                TourInformation tourInformation = tourInformationDao.findOrderGoodAllInfo(ordergood);
                TourInformations.add(tourInformation);
            }
        }
    }

    class MyBaseAdapater extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return TourInformations.size();
        }

        @Override
        public TourInformation getItem(int i)
        {
            return TourInformations.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup)
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_myfragmentlistviewitem_content, null, false);
                holder.ll_tourinfo = (LinearLayout) convertView.findViewById(R.id.ll_tourinfo);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_tourname = (TextView) convertView.findViewById(R.id.tv_tourname);
                holder.tv_startplace = (TextView) convertView.findViewById(R.id.tv_startplace);
                holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_tourinfo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("icon", TourInformations.get(position).getIcon());
                    bundle.putString("tourname", TourInformations.get(position).getTourname());
                    bundle.putString("startplace", TourInformations.get(position).getStartplace());
                    bundle.putString("price", TourInformations.get(position).getPrice());
                    bundle.putString("tour1content", TourInformations.get(position).getTour1content());
                    bundle.putString("tour2content", TourInformations.get(position).getTour2content());
                    bundle.putInt("upremark", TourInformations.get(position).getUpremark());
                    bundle.putInt("downremark", TourInformations.get(position).getDownremark());
                    bundle.putString("detailcontent", TourInformations.get(position).getDetailcontent());
                    bundle.putString("costcontent", TourInformations.get(position).getCostcontent());
                    showPopupWindow(view, bundle);
                }
            });
            Bitmap bitmap = ImageUtils.intToBitmap(UIUtils.getContext(), getItem(position).getIcon());
            if (bitmap == null)
            {
                //重sd卡中读取文件bitmap
                int icon = getItem(position).getIcon();
                bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath + "/Picture" + "/" + String.valueOf(icon) + ".png");
            }
            //LruCache内存优化
            if (mMemoryCache != null)
            {
                if (bitmap != null)
                {
                    mMemoryCache.setMemoryCache(getItem(position).getTourname(), bitmap);
                    holder.iv_icon.setImageBitmap(mMemoryCache.getMemoryCache(getItem(position).getTourname()));
                }
                else
                {
                    //如果在lru缓存和sd卡内存中都没有找到就给设置一个默认的背景
                    Bitmap bitmap1 = ImageUtils.intToBitmap(UIUtils.getContext(), R.mipmap.banner1);
                    mMemoryCache.setMemoryCache(getItem(position).getTourname(), bitmap1);
                    holder.iv_icon.setImageBitmap(mMemoryCache.getMemoryCache(getItem(position).getTourname()));
                }
            }
            //holder.iv_icon.setBackgroundResource(TourInformations.get(position).getIcon());
            holder.tv_tourname.setText(TourInformations.get(position).getTourname());
            holder.tv_startplace.setText("跟团游|" + TourInformations.get(position).getStartplace());
            holder.tv_price.setText(TourInformations.get(position).getPrice());

            return convertView;
        }
    }

    private void showPopupWindow(View view, final Bundle bundle)
    {
        View popupView = View.inflate(getContext(), R.layout.popupwindow_layout, null);
        TextView tv_detail = (TextView) popupView.findViewById(R.id.tv_detail);
        TextView tv_discuss = (TextView) popupView.findViewById(R.id.tv_discuss);
        TextView tv_delete = (TextView) popupView.findViewById(R.id.tv_delete);
        //详情
        tv_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Env.startActivity(getContext(), DetailsActivity.class, bundle);
            }
        });
        //评论
        tv_discuss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //这块设计应该跳入游客点评界面
                Bundle bundle1 = new Bundle();
                bundle1.putString("tourname", bundle.getString("tourname"));
                Env.startActivity(getContext(), DiscussActivity.class, bundle1);

            }
        });
        //删除订购资讯
        tv_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {   //1拿到订购资讯的名字
                String orderGood = bundle.getString("tourname");
                //2.对话框显示
                //3.在订购资讯数据库中删除当前用户订购的此项资讯
                showDialog(orderGood);
                //4.跟新ui呗
            }
        });

        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.showAsDropDown(view, view.getWidth() * 3 / 5, -view.getHeight() / 3);
        //动画设置
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(400);
        alphaAnimation.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        scaleAnimation.setFillAfter(true);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        popupView.startAnimation(animationSet);


    }

    private void showDialog(final String orderGood)
    {
        final String currentUser = SpUtils.getString(getContext(), ConstantValues.CURRENT_USER, "");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示信息");
        builder.setMessage("确定要删除此订购？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                mPopupWindow.dismiss();//防止popupwindow多次被点击出现界面刷新bug
                //3.在订购资讯数据库中删除当前用户订购的此项资讯
                userInformationDao.deleteUserOrderGoods(currentUser, orderGood);
                //删除之后跟新ui
                updateData();
                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });
        builder.create().show();

    }

    static class ViewHolder
    {
        ImageView iv_icon;
        TextView tv_tourname;
        TextView tv_startplace;
        TextView tv_price;
        LinearLayout ll_tourinfo;
    }

    class OrderGoodsBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {    //接收到广播更新UI
            if (ConstantValues.OrderGoodsSuccess.equals(intent.getAction()))
            {   //这块应该是跟新一下数据
                updateData();
            }

        }
    }

    class DeletePublishBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            updateData();
        }
    }

}
