package com.example.administrator.ewmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.engine.PublishTourInformation;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.FileUtils;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.MemoryCacheUtils;
import com.example.administrator.ewmarket.app.utils.SpUtils;
import com.example.administrator.ewmarket.app.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class MyPublishActivity extends Activity implements View.OnClickListener
{
    private ImageView iv_btn_back;
    private ListView lv_mylistview;
    private ArrayList<TourInformation> mPublishTourInformations;
    private String currentUser;
    private MyBaseAdapater mBaseAdapater;
    private MemoryCacheUtils mMemoryCache;
    private String mSdFileAbsolutePath = UIUtils.getContext().getExternalFilesDir(null).getAbsolutePath();
    private PopupWindow mPopupWindow;

    private Handler mUpdataHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (mBaseAdapater != null)
            {
                mBaseAdapater.notifyDataSetChanged();
            }
        }
    };
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            mBaseAdapater = new MyBaseAdapater();
            lv_mylistview.setAdapter(mBaseAdapater);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypublish);
        currentUser = SpUtils.getString(getApplicationContext(), ConstantValues.CURRENT_USER, "");
        mMemoryCache = new MemoryCacheUtils(1);
        initView();
        initData();
    }

    private void initView()
    {
        iv_btn_back = (ImageView) findViewById(R.id.iv_btn_back);
        lv_mylistview = (ListView) findViewById(R.id.lv_mylistview);
        iv_btn_back.setOnClickListener(this);
    }

    private void initData()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                TourInformationDao tourInformationDao = TourInformationDao.getInstance(getApplicationContext());
                mPublishTourInformations = (ArrayList<TourInformation>) tourInformationDao.findPublishInfoAll(currentUser);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void updata()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                TourInformationDao tourInformationDao = TourInformationDao.getInstance(getApplicationContext());
                mPublishTourInformations = (ArrayList<TourInformation>) tourInformationDao.findPublishInfoAll(currentUser);
                mUpdataHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onClick(View view)
    {
        finish();
    }

    class MyBaseAdapater extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mPublishTourInformations.size();
        }

        @Override
        public TourInformation getItem(int position)
        {
            return mPublishTourInformations.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup)
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_destinationlistviewitem_content, null, false);
                holder.ll_tourinfo = (LinearLayout) convertView.findViewById(R.id.ll_tourinfo);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_tourname = (TextView) convertView.findViewById(R.id.tv_tourname);
                holder.tv_startplace = (TextView) convertView.findViewById(R.id.tv_startplace);
                holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                holder.tv_tourcontent1 = (TextView) convertView.findViewById(R.id.tv_tourcontent1);
                holder.tv_tourcontent2 = (TextView) convertView.findViewById(R.id.tv_tourcontent2);
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
                    bundle.putInt("icon", mPublishTourInformations.get(position).getIcon());
                    bundle.putString("tourname", mPublishTourInformations.get(position).getTourname());
                    bundle.putString("startplace", mPublishTourInformations.get(position).getStartplace());
                    bundle.putString("price", mPublishTourInformations.get(position).getPrice());
                    bundle.putString("tour1content", mPublishTourInformations.get(position).getTour1content());
                    bundle.putString("tour2content", mPublishTourInformations.get(position).getTour2content());
                    bundle.putInt("upremark", mPublishTourInformations.get(position).getUpremark());
                    bundle.putInt("downremark", mPublishTourInformations.get(position).getDownremark());
                    bundle.putString("detailcontent", mPublishTourInformations.get(position).getDetailcontent());
                    bundle.putString("costcontent", mPublishTourInformations.get(position).getCostcontent());
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
            //holder.iv_icon.setBackgroundResource(mPublishTourInformations.get(position).getIcon());
            holder.tv_tourname.setText(mPublishTourInformations.get(position).getTourname());
            holder.tv_startplace.setText("跟团游|" + mPublishTourInformations.get(position).getStartplace());
            holder.tv_price.setText(mPublishTourInformations.get(position).getPrice());
            holder.tv_tourcontent1.setText(mPublishTourInformations.get(position).getTour1content());
            holder.tv_tourcontent2.setText(mPublishTourInformations.get(position).getTour2content());
            return convertView;
        }
    }

    static class ViewHolder
    {
        ImageView iv_icon;
        TextView tv_tourname;
        TextView tv_startplace;
        TextView tv_price;
        TextView tv_tourcontent1;
        TextView tv_tourcontent2;
        LinearLayout ll_tourinfo;
    }

    private void showPopupWindow(View view, final Bundle bundle)
    {
        View popupView = View.inflate(getApplicationContext(), R.layout.publish_popupwindow_layout, null);
        TextView tv_detail = (TextView) popupView.findViewById(R.id.tv_detail);
        TextView tv_delete = (TextView) popupView.findViewById(R.id.tv_delete);
        //详情
        tv_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Env.startActivity(getApplicationContext(), DetailsActivity.class, bundle);
            }
        });
        //删除发布资讯
        tv_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String publishInfoTourname = bundle.getString("tourname");
                int icon = bundle.getInt("icon");
                showDialog(publishInfoTourname, icon);
            }
        });

        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.showAsDropDown(view, view.getMeasuredWidth() * 4 / 5, -view.getHeight() / 4);
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

    private void showDialog(final String publishInfoTourname, final int icon)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息");
        builder.setMessage("确定要删除此发布的资讯？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //                TourInformationDao tourInformationDao=TourInformationDao.getInstance(getApplicationContext());
                //                tourInformationDao.deletePublishInfo(currentUser,publishInfoTourname);
                //发现 PopupWindow删的太快会出现这种空指针的bug。//解决办法点击之后让popupWindow立刻消失
                mPopupWindow.dismiss();//完美解决
                PublishTourInformation.deletePublishinfo(getApplicationContext(), currentUser, publishInfoTourname);
                FileUtils.deleteFile(new File(mSdFileAbsolutePath + "/Picture" + "/" + String.valueOf(icon) + ".png"));
                Toast.makeText(getApplicationContext(), "删除发布资讯成功", Toast.LENGTH_SHORT).show();
                //通知更新ui
                updata();//就是这块，开启线程跟新ui，当删除按钮没消失，你快速按一下就会出现listview刷新空指针异常
                //发送删除成功广播
                Intent intent = new Intent();
                intent.setAction(ConstantValues.DeletePublishSuccess);
                intent.putExtra("tourname", publishInfoTourname);
                sendBroadcast(intent);
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
}
