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
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.View.RoundImageView;
import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @author by
 */

public class SelectedUserIconActivity extends Activity implements View.OnClickListener
{
    private RoundImageView iv_usericon;
    private TextView tv_selected;
    private TextView tv_confirm;
    private TextView tv_icon1;
    private TextView tv_icon2;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int SCALE = 6;//照片缩小比例
    private Bitmap mCurrentSmallBitmap;
    private final int typePhotoSelectIcon = 1;
    private int currentSelectIcon = R.color.app_theme;
    private String mSdFileAbsolutePath =UIUtils.getContext().getExternalFilesDir(null).getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedusericon);
        initView();

    }

    private void initView()
    {
        iv_usericon = (RoundImageView) findViewById(R.id.iv_usericon);
        tv_icon1 = (TextView) findViewById(R.id.tv_icon1);
        tv_icon2 = (TextView) findViewById(R.id.tv_icon2);
        tv_selected = (TextView) findViewById(R.id.tv_selected);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);

        tv_icon1.setOnClickListener(this);
        tv_icon2.setOnClickListener(this);
        tv_selected.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view == tv_icon1)
        {
            iv_usericon.setImageResource(R.mipmap.userlufei);
            currentSelectIcon = R.mipmap.userlufei;
        }
        if (view == tv_icon2)
        {
            iv_usericon.setImageResource(R.mipmap.usershanzhi);
            currentSelectIcon = R.mipmap.usershanzhi;
        }
        if (view == tv_selected)
        {
            showPicturePicker(this);
        }
        if (view == tv_confirm)
        {
            confirm();
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
                    iv_usericon.setImageBitmap(newBitmap);
                    //ImageUtils.savePhotoToSDCard(newBitmap,mSdFileAbsolutePath+ "/Picture", String.valueOf(System.currentTimeMillis()));
                    mCurrentSmallBitmap = newBitmap;
                    currentSelectIcon = typePhotoSelectIcon;
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

                            iv_usericon.setImageBitmap(smallBitmap);
                            mCurrentSmallBitmap = smallBitmap;
                            currentSelectIcon = typePhotoSelectIcon;
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

    private void confirm()
    {
        final UserInformationDao userInformationDao = UserInformationDao.getInstance(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息");
        builder.setMessage("是否确定选择?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (currentSelectIcon == R.color.app_theme)
                {
                    Toast.makeText(getApplicationContext(), "对不起请选择头像", Toast.LENGTH_SHORT).show();
                }
                if (currentSelectIcon == R.mipmap.userlufei)
                {
                    String username = getIntent().getStringExtra("username");
                    String password = getIntent().getStringExtra("password");
                    userInformationDao.addUser(username, password, R.mipmap.userlufei);
                    Toast.makeText(UIUtils.getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (currentSelectIcon == R.mipmap.usershanzhi)
                {
                    String username = getIntent().getStringExtra("username");
                    String password = getIntent().getStringExtra("password");
                    userInformationDao.addUser(username, password, R.mipmap.usershanzhi);
                    Toast.makeText(UIUtils.getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

                if (currentSelectIcon == typePhotoSelectIcon)
                {
                    currentSelectIcon = (int) System.currentTimeMillis();
                    if (mCurrentSmallBitmap != null)
                        ImageUtils.savePhotoToSDCard(mCurrentSmallBitmap, mSdFileAbsolutePath + "/UserIconPicture", String.valueOf(currentSelectIcon));
                    String username = getIntent().getStringExtra("username");
                    String password = getIntent().getStringExtra("password");
                    userInformationDao.addUser(username, password,currentSelectIcon);
                    Toast.makeText(UIUtils.getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
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
