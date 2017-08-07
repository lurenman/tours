package com.example.administrator.ewmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.View.MyScrollview;
import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.engine.TourDiscuss;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.SpUtils;
import com.example.administrator.ewmarket.app.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class DetailsActivity extends Activity implements View.OnClickListener
{   private ImageView iv_btn_back;
    private TextView tv_title;
    private RelativeLayout titlebar;
    private ImageView iv_icon;
    private TextView tv_tourname;
    private TextView  tv_startplace;
    private TextView  tv_price;
    private TextView tv_tourcontent1;
    private TextView tv_tourcontent2;
    private TextView tv_upremark;
    private TextView tv_downremark;
    private TextView tv_description;
    private MyScrollview mScrollview;
    private TextView tv_costcontent;
    private TextView tv_costprice;
    private TextView tv_ordergoods;
    private TextView tv_discuss;
    private TextView tv_discusscount;

    private String currentUser;
    private UserInformationDao userInformationDao;
    private TourInformationDao tourInformationDao;
    private  String tourname;
    private int upremark;
    private int downremark;

    //是否订购过此资讯
    private Boolean isOrderGoods=false;
    //是否点赞或鄙视过此资讯
    private Boolean isUpRemark=false;
    private Boolean isDownRemark=false;
    private ArrayList<TourDiscuss> mDiscussContents;
    private String mSdFileAbsolutePath= UIUtils.getContext().getExternalFilesDir(null).getAbsolutePath();
    private Handler mDiscussCountHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            tv_discusscount.setText(String.format("点评数（%d）",mDiscussContents.size()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        currentUser= SpUtils.getString(getApplicationContext(),ConstantValues.CURRENT_USER,"");
        userInformationDao=UserInformationDao.getInstance(getApplicationContext());
        tourInformationDao= TourInformationDao.getInstance(getApplicationContext());
        initView();
        initData();
        isOrderGoods=userInformationDao.findOrderGoods(currentUser,tourname);
       if (isOrderGoods==true)
       {
           tv_ordergoods.setText("已订购");
           tv_ordergoods.setClickable(false);
       }
        else
       {   //果如用户没有订购过此资讯就不让点
           tv_upremark.setClickable(false);
           tv_downremark.setClickable(false);
       }
        //如果用户点赞过就不让点了
        isUpRemark=userInformationDao.findUpRemark(currentUser,tourname,"yes");
        isDownRemark=userInformationDao.findDownRemark(currentUser,tourname,"yes");
        //下面的选中标识就是代表你当前用户点赞还是鄙视过，尽管你删除此资讯还是能看到你是否点赞过
        //如果删除订购资讯在订购还是可以有点赞和鄙视的功能。
        //点赞和鄙视的功能在用户每次订购中只能做一次点赞或鄙视，和内涵段子差不多（模仿）
        if (isUpRemark==true)
        {
            tv_upremark.setSelected(true);
            tv_upremark.setClickable(false);
            tv_downremark.setClickable(false);
        }
        if (isDownRemark==true)
        {
            tv_downremark.setSelected(true);
            tv_upremark.setClickable(false);
            tv_downremark.setClickable(false);
        }
    }
    private void initView()
    {
        iv_btn_back=(ImageView)this.findViewById(R.id.iv_btn_back);
        iv_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        titlebar = (RelativeLayout)this.findViewById(R.id.titlebar_layout);
        titlebar.getBackground().setAlpha(0);
        tv_title = (TextView)this.findViewById(R.id.tv_title);
        iv_icon=(ImageView) this.findViewById(R.id.iv_icon);
        tv_tourname=(TextView)this.findViewById(R.id.tv_tourname);
        tv_startplace=(TextView)this.findViewById(R.id.tv_startplace);
        tv_price=(TextView)this.findViewById(R.id.tv_price);
        tv_tourcontent1=(TextView)this.findViewById(R.id.tv_tourcontent1);
        tv_tourcontent2=(TextView)this.findViewById(R.id.tv_tourcontent2);
        tv_upremark=(TextView)this.findViewById(R.id.tv_upremark);
        tv_downremark=(TextView)this.findViewById(R.id.tv_downremark);
        tv_description=(TextView)this.findViewById(R.id.tv_description);
        tv_costcontent=(TextView)this.findViewById(R.id.tv_costcontent);
        tv_costprice=(TextView)this.findViewById(R.id.tv_costprice);
        tv_ordergoods=(TextView)this.findViewById(R.id.tv_ordergoods);
        tv_discuss=(TextView)this.findViewById(R.id.tv_discuss);
        tv_discusscount=(TextView)this.findViewById(R.id.tv_discusscount);

        tv_ordergoods.setOnClickListener(this);
        tv_discuss.setOnClickListener(this);
        tv_upremark.setOnClickListener(this);
        tv_downremark.setOnClickListener(this);
        mScrollview = (MyScrollview)this.findViewById(R.id.my_scrollView);
        // 实现自定义控件中的监听器
        mScrollview.setScrollViewListener(new MyScrollview.ScrollViewListener()
        {
            @Override
            public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy)
            {
                titleAnima(y);
            }
        });

    }


    private void initData()
    {   Intent intent = this.getIntent();
        Bundle bundle = null;
        if (intent != null && (bundle = intent.getExtras()) != null)
        {
            int icon=bundle.getInt("icon");
            tourname=bundle.getString("tourname");
            String startplace=bundle.getString("startplace");
            String price=bundle.getString("price");
            String tour1content=bundle.getString("tour1content");
            String tour2content=bundle.getString("tour2content");
//            upremark=bundle.getInt("upremark");
//            downremark=bundle.getInt("downremark");
            upremark=tourInformationDao.findUpremark(tourname);
            downremark=tourInformationDao.findDownremark(tourname);
            String detailcontent=bundle.getString("detailcontent");
            String costcontent=bundle.getString("costcontent");
            Bitmap bitmap = ImageUtils.intToBitmap(UIUtils.getContext(), icon);
            if (bitmap==null)
            {
                bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath+"/Picture"+"/"+String.valueOf(icon)+".png");
            }
            iv_icon.setImageBitmap(bitmap);
            tv_tourname.setText(tourname);
            tv_startplace.setText("跟团游|"+startplace);
            tv_price.setText(price);
            tv_tourcontent1.setText(tour1content);
            tv_tourcontent2.setText(tour2content);
            tv_upremark.setText(Integer.toString(upremark));
            tv_downremark.setText(Integer.toString(downremark));
            tv_title.setText(tourname);
            tv_description.setText("产品特色\n"+detailcontent);
            tv_costcontent.setText(costcontent);
            tv_costprice.setText(price);

            mDiscussContents=(ArrayList<TourDiscuss>)userInformationDao.findCurrentTourDiscussInfo(tourname);
            tv_discusscount.setText(String.format("点评数（%d）",mDiscussContents.size()));
           //这块开启线程更新ui有时会蹦，不解待研究
           // findDiscussCount();
        }

    }
    private void findDiscussCount()
    {
        new Thread(){
            @Override
            public void run()
            {
                mDiscussContents=(ArrayList<TourDiscuss>)userInformationDao.findCurrentTourDiscussInfo(tourname);
                mDiscussCountHandler.sendEmptyMessage(0);
            }
        }.start();
    }
    /**
     * 出现渐变效果
     */
    public void titleAnima(int y)
    {
        int height = iv_icon.getMeasuredHeight()/2;
        if (y >= 0 && y <= height)
        {
            float scrollPercent = (float) y / height;
            titlebar.getBackground().setAlpha((int) (255 * scrollPercent));
            tv_title.setTextColor(Color.argb((int) scrollPercent * 255, 255, 255, 255));

        }
        else
        {
            //这两句代码不写title可能消失或随机出现，titleBar可能透明度随机。
            titlebar.getBackground().setAlpha((int) (255 * 1));
            tv_title.setTextColor(Color.argb((int) 255, 255, 255, 255));
        }
    }

    @Override
    public void onClick(View view)
    {
       if (view==tv_ordergoods)
       {
           AlertDialog.Builder builder=new AlertDialog.Builder(this);
           builder.setTitle("提示信息");
           builder.setMessage("确定要订购此资讯吗？");
           builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i)
               {   //订购资讯
                   orderGoodsTourInfromation();
               }
           });
           builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i)
               {

               }
           });
           builder.create().show();
       }
        //游客点评跳转的activity
        if (view==tv_discuss)
        {   Bundle bundle=new Bundle();
            bundle.putString("tourname",tourname);
            //Env.startActivity(getApplicationContext(),DiscussActivity.class,bundle);
            Intent intent=new Intent(this,DiscussActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,0);
        }
        if (view==tv_upremark)
        {
            //1.这块里跟新那个资讯upremark次数
            //2.这块里判断一下当前用户是否已经点赞或鄙视，如果点赞或鄙视就不让点了，这块
            tourInformationDao.updateUpremark(tourname,upremark+1);
            userInformationDao.addRemark(currentUser,tourname,"yes","no");
            //点赞完之后点赞和鄙视按钮不可在点击
            tv_upremark.setSelected(true);
            tv_upremark.setClickable(false);
            tv_downremark.setClickable(false);
            //跟新ui
            tv_upremark.setText(Integer.toString(upremark+1));

        }
        if (view==tv_downremark)
        {
            tourInformationDao.updateDownremark(tourname,downremark+1);
            userInformationDao.addRemark(currentUser,tourname,"no","yes");
            tv_downremark.setSelected(true);
            tv_downremark.setClickable(false);
            tv_upremark.setClickable(false);
            tv_downremark.setText(Integer.toString(downremark+1));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        int discusscount=data.getIntExtra("discusscount",mDiscussContents.size());
        tv_discusscount.setText(String.format("点评数（%d）",discusscount));
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void orderGoodsTourInfromation()
    {
        currentUser= SpUtils.getString(getApplicationContext(),ConstantValues.CURRENT_USER,"");
        if (currentUser.equals(""))
        {
            Toast.makeText(getApplicationContext(),"对不起请先登录才可以订购",Toast.LENGTH_LONG).show();
        }else if (!currentUser.equals(""))
        {
            //1.先判断是否订购过此资讯
            //2.获取此订购资讯的tourname
            if (isOrderGoods==true)
            {
                Toast.makeText(getApplicationContext(),"对不起你已经订购了此资讯",Toast.LENGTH_LONG).show();
            }
            else
            {
                //3.如果没有找到那就给表插入此资讯
                if (userInformationDao!=null){
                    userInformationDao.addUserOrderGoods(currentUser,tourname);
                    Toast.makeText(getApplicationContext(),"恭喜你成功订购此资讯",Toast.LENGTH_LONG).show();
                    tv_ordergoods.setText("已订购");
                    tv_ordergoods.setClickable(false);
                    tv_upremark.setClickable(true);
                    tv_downremark.setClickable(true);
                    //4.此时该通知用户更新订购UI
                    //发送自定义的广播
                    Intent intent=new Intent();
                    intent.setAction(ConstantValues.OrderGoodsSuccess);
                    sendBroadcast(intent);
                }
            }
        }
    }
}
