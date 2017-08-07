package com.example.administrator.ewmarket.app.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。Google建议使用LruCache
 */
public class MemoryCacheUtils
{

	private LruCache<String, Bitmap> mMemoryCache;
	private final static int typeMyFragment=0;
	private int denominator;

	public MemoryCacheUtils(int type){
		long maxMemory = Runtime.getRuntime().maxMemory();// 获取分配给app的内存大小
		System.out.println("maxMemory:" + maxMemory);
		if (type==typeMyFragment)
		{
			denominator=16;
		}else {
			denominator=8;
		}
		if (denominator!=0)
		{
			mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / denominator)) {

				// 返回每个对象的大小
				@Override
				protected int sizeOf(String key, Bitmap value) {
					// int byteCount = value.getByteCount();
					int byteCount = value.getRowBytes() * value.getHeight();// 计算图片大小:每行字节数*高度
					return byteCount;
				}
			};
		}

	}

	/**
	 * 写缓存
	 */
	public void setMemoryCache(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
	}

	/**
	 * 读缓存
	 */
	public Bitmap getMemoryCache(String url){
		return mMemoryCache.get(url);
	}
}
