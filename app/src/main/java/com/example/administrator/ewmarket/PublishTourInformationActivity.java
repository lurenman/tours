package com.example.administrator.ewmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.engine.PublishTourInformation;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.SpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class PublishTourInformationActivity extends Activity implements View.OnClickListener
{
    private ImageView iv_btn_back;
    //下面是6个描述资讯内容的
    private EditText et_tourname;
    private EditText et_startplace;
    private EditText et_price;
    private EditText et_tour1content;
    private EditText et_tour2content;
    private EditText et_detailcontent;
    //资讯选择iamgeView
    private ImageView iv_touricon;
    //样图1
    private TextView tv_icon1;
    //样图2
    private TextView tv_icon2;
    //默认蓝色背景imageview
    private TextView tv_icon3;
    //发布
    private TextView tv_publish;
    private int currentSelectIcon = R.drawable.bg_corner_app_theme;
    private String currentUser;
    private ArrayList<String> allTourNames;
    private TourInformationDao tourInformationDao;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;

    private static final int SCALE = 6;//照片缩小比例
    private String mSdFileAbsolutePath;
    private Bitmap mCurrentSmallBitmap;
    private final int typePhotoSelectIcon=1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishtourinformation);
        tourInformationDao = TourInformationDao.getInstance(getApplicationContext());
        currentUser = SpUtils.getString(getApplicationContext(), ConstantValues.CURRENT_USER, "");
        mSdFileAbsolutePath = getExternalFilesDir(null).getAbsolutePath();
        initView();
    }

    private void initView()
    {
        iv_btn_back = (ImageView) findViewById(R.id.iv_btn_back);
        et_tourname = (EditText) findViewById(R.id.et_tourname);
        et_startplace = (EditText) findViewById(R.id.et_startplace);
        et_price = (EditText) findViewById(R.id.et_price);
        et_tour1content = (EditText) findViewById(R.id.et_tour1content);
        et_tour2content = (EditText) findViewById(R.id.et_tour2content);
        et_detailcontent = (EditText) findViewById(R.id.et_detailcontent);
        iv_touricon = (ImageView) findViewById(R.id.iv_touricon);
        tv_icon1 = (TextView) findViewById(R.id.tv_icon1);
        tv_icon2 = (TextView) findViewById(R.id.tv_icon2);
        tv_icon3 = (TextView) findViewById(R.id.tv_icon3);
        tv_publish = (TextView) findViewById(R.id.tv_publish);

        iv_btn_back.setOnClickListener(this);
        tv_icon1.setOnClickListener(this);
        tv_icon2.setOnClickListener(this);
        tv_icon3.setOnClickListener(this);
        tv_publish.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if (view == iv_btn_back)
        {
            finish();
        }
        else if (view == tv_icon1)
        {
            iv_touricon.setBackgroundResource(R.mipmap.banner1);
            currentSelectIcon = R.mipmap.banner1;
        }
        else if (view == tv_icon2)
        {
            iv_touricon.setBackgroundResource(R.mipmap.banner3);
            currentSelectIcon =R.mipmap.banner3;
        }
        else if (view == tv_icon3)
        {
            //            iv_touricon.setBackgroundResource(R.color.app_theme);
            //            currentSelectIcon=R.color.app_theme;
            showPicturePicker(this);
        }
        else if (view == tv_publish)
        {
            if (isCheckExist())
            {
                Toast.makeText(getApplicationContext(), "对不起资讯名字不能重复", Toast.LENGTH_SHORT).show();
            }
            else
            {
                checkInfo();
            }
        }
    }

    public void showPicturePicker(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = Uri.fromFile(new File(mSdFileAbsolutePath, "image.jpg"));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath + "/image.jpg");
                    Bitmap newBitmap = ImageUtils.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
                    if (mCurrentSmallBitmap != newBitmap && mCurrentSmallBitmap != null)
                    {   //这次是你重新选择照片时候把上次缩小的图片释放掉
                        mCurrentSmallBitmap.recycle();
                    }
                    //将处理过的图片显示在界面上，并保存到本地
                    iv_touricon.setImageBitmap(newBitmap);
                   // ImageUtils.savePhotoToSDCard(newBitmap,mSdFileAbsolutePath+ "/Picture", String.valueOf(System.currentTimeMillis()));
                    mCurrentSmallBitmap = newBitmap;
                    currentSelectIcon=typePhotoSelectIcon;
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try
                    {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null)
                        {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageUtils.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            if (mCurrentSmallBitmap != smallBitmap && mCurrentSmallBitmap != null)
                            {   //这次是你重新选择照片时候把上次缩小的图片释放掉
                                mCurrentSmallBitmap.recycle();
                            }

                            iv_touricon.setImageBitmap(smallBitmap);
                            mCurrentSmallBitmap = smallBitmap;
                            currentSelectIcon=typePhotoSelectIcon;
                        }
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private Boolean isCheckExist()
    {
        Boolean isExist = false;
        allTourNames = (ArrayList<String>) tourInformationDao.findAllTourName();
        int i = 0;
        for (i = 0; i < allTourNames.size(); i++)
        {
            if (allTourNames.get(i).equals(et_tourname.getText().toString().trim()))
            {
                isExist = true;
            }
        }
        return isExist;
    }

    private void checkInfo()
    {
        if (!TextUtils.isEmpty(et_tourname.getText().toString().trim()) &&
                !TextUtils.isEmpty(et_startplace.getText().toString().trim())
                && !TextUtils.isEmpty(et_price.getText().toString().trim())
                && !TextUtils.isEmpty(et_tour1content.getText().toString().trim())
                && !TextUtils.isEmpty(et_tour2content.getText().toString().trim())
                && !TextUtils.isEmpty(et_detailcontent.getText().toString().trim())&&currentSelectIcon!= R.drawable.bg_corner_app_theme)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示信息");
            builder.setMessage("是否确定要发布此条资讯");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (currentSelectIcon==typePhotoSelectIcon)
                    {
                        currentSelectIcon = (int) System.currentTimeMillis();
                        if (mCurrentSmallBitmap != null)
                            ImageUtils.savePhotoToSDCard(mCurrentSmallBitmap, mSdFileAbsolutePath + "/Picture", String.valueOf(currentSelectIcon));
                    }
                    //这块就要把信息发出去跟新数据库
                    PublishTourInformation.publish(getApplicationContext(), currentUser, et_tourname.getText().toString(), et_startplace.getText().toString(), et_price.getText().toString(), et_tour1content.getText().toString(), et_tour2content.getText().toString(), et_detailcontent.getText().toString(), currentSelectIcon);
                    //发送广播
                    Intent intent = new Intent();
                    intent.setAction(ConstantValues.PublishSuccess);
                    sendBroadcast(intent);
                    Toast.makeText(getApplicationContext(), "发布资讯成功", Toast.LENGTH_SHORT).show();

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
        else
        {
            Toast.makeText(getApplicationContext(), "对不起请输入完整的资讯信息", Toast.LENGTH_SHORT).show();
        }
    }

}
