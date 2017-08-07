package com.example.administrator.ewmarket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.utils.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class SearchActivity extends Activity implements View.OnClickListener
{   private ImageView btn_back;
    private EditText et_input;
    private ImageView iv_clear_input;
    private ImageView iv_search_go;
    private ListView lv_listview;
    private TourInformationDao tourInformationDao;
    private ArrayList<String> allTourNames;
    private ArrayList<TourInformation> tourInformations;
    private  mBaseAdapter mBaseAdapter;
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            lv_listview.setAdapter(mBaseAdapter);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        tourInformationDao=TourInformationDao.getInstance(getApplicationContext());
        //查询出数据库中tournamen列的所有旅游名字
        allTourNames = (ArrayList<String>)tourInformationDao.findAllTourName();
      //  tourInformations=new ArrayList<TourInformation>();
        mBaseAdapter = new mBaseAdapter();

    }

    private void initView()
    {
        btn_back=(ImageView)findViewById(R.id.btn_back);
        et_input=(EditText)findViewById(R.id.et_input);
        iv_clear_input=(ImageView)findViewById(R.id.iv_clear_input);
        iv_search_go=(ImageView)findViewById(R.id.iv_search_go);
        lv_listview=(ListView)findViewById(R.id.lv_listview);


        btn_back.setOnClickListener(this);
        iv_clear_input.setOnClickListener(this);
        iv_search_go.setOnClickListener(this);
        //实现动态查询
        et_input.addTextChangedListener(new mTextWatcher());
    }

    @Override
    public void onClick(View view)
    {
        if (view==btn_back)
        {
            finish();
        }
        if (view==iv_clear_input)
        {
            et_input.setText("");
        }
        if (view==iv_search_go)
        {
          searchGo();
        }
    }
    private void searchGo()
    {  //这块发现一个bug当空字符串搜索的时候发现或点几个空格搜索的时候会把所有资讯搜索出来，说明字符串都是包含""这个空字符的
        if (!et_input.getText().toString().trim().equals(""))
        {
            //每次点击搜索按钮的时候都要把tourInformation集合从新分配一下这样才可以避免集合中保留上次搜索的记录
            tourInformations=new ArrayList<TourInformation>();
            if (tourInformationDao!=null&&allTourNames.size()!=0)
            {
                int i=0;
                for (i=0;i<allTourNames.size();i++)
                {
                    if (allTourNames.get(i).contains(et_input.getText().toString().trim()))
                    {   //查询某条资讯的所有信息，findOrderGoodAllInfo这个命名有点不妥就先这样吧
                        TourInformation tourInformation= tourInformationDao.findOrderGoodAllInfo(allTourNames.get(i));
                        tourInformations.add(tourInformation);
                        lv_listview.setAdapter(mBaseAdapter);
                       // queryData(i);
                    }

                }

            }
        }else
        {
            //此时是搜索栏为空的时候
            tourInformations=new ArrayList<TourInformation>();
            lv_listview.setAdapter(mBaseAdapter);

        }

    }
    private void queryData(final int i)
    {
        new Thread(){
            @Override
            public void run()
            {
                TourInformation tourInformation= tourInformationDao.findOrderGoodAllInfo(allTourNames.get(i));
                tourInformations.add(tourInformation);
                mHandler.sendEmptyMessage(0);
                super.run();
            }
        }.start();
    }
    class mTextWatcher implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            searchGo();
        }
    }
    class mBaseAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return tourInformations.size();
        }

        @Override
        public Object getItem(int position)
        {
            return tourInformations.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup)
        {
            ViewHolder holder= null;
            if (convertView==null)
            {
                holder=new ViewHolder();
                convertView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_searchlistviewitem_content,null,false);
                holder.ll_tourinfo=(LinearLayout)convertView.findViewById(R.id.ll_tourinfo);
                holder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_tourname=(TextView)convertView.findViewById(R.id.tv_tourname);
                holder.tv_startplace=(TextView)convertView.findViewById(R.id.tv_startplace);
                holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
                holder.tv_tourcontent1=(TextView)convertView.findViewById(R.id.tv_tourcontent1);
                holder.tv_tourcontent2=(TextView)convertView.findViewById(R.id.tv_tourcontent2);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            holder.ll_tourinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle=new Bundle();
                    bundle.putInt("icon",tourInformations.get(position).getIcon());
                    bundle.putString("tourname",tourInformations.get(position).getTourname());
                    bundle.putString("startplace",tourInformations.get(position).getStartplace());
                    bundle.putString("price",tourInformations.get(position).getPrice());
                    bundle.putString("tour1content",tourInformations.get(position).getTour1content());
                    bundle.putString("tour2content",tourInformations.get(position).getTour2content());
                    bundle.putInt("upremark",tourInformations.get(position).getUpremark());
                    bundle.putInt("downremark",tourInformations.get(position).getDownremark());
                    bundle.putString("detailcontent",tourInformations.get(position).getDetailcontent());
                    bundle.putString("costcontent",tourInformations.get(position).getCostcontent());
                    Env.startActivity(getApplicationContext(), DetailsActivity.class,bundle);
                }
            });
            holder.iv_icon.setBackgroundResource(tourInformations.get(position).getIcon());
            holder.tv_tourname.setText(tourInformations.get(position).getTourname());
            holder.tv_startplace.setText("跟团游|"+tourInformations.get(position).getStartplace());
            holder.tv_price.setText(tourInformations.get(position).getPrice());
            holder.tv_tourcontent1.setText(tourInformations.get(position).getTour1content());
            holder.tv_tourcontent2.setText(tourInformations.get(position).getTour2content());

            return convertView;
        }
    }
    static class ViewHolder
    {   ImageView iv_icon;
        TextView tv_tourname;
        TextView tv_startplace;
        TextView tv_price;
        TextView tv_tourcontent1;
        TextView tv_tourcontent2;
        LinearLayout ll_tourinfo;
    }

}
