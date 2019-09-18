/*
 * ========================================================
 * Copyright(c) 2012 杭州龙骞科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.hzdracom.com/
 * 
 * ========================================================
 */

package com.android.agnetty.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;
import android.view.View.MeasureSpec;

import com.android.agnetty.constant.FileCst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 图片处理工具类
 */
public class ImageUtil {

	/**
	 * 位图转为字节数组
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap bitmap) {
        if (bitmap == null) return null;

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOut);
        return byteOut.toByteArray();
    }
	
	/**
	 * 字节数组转为位图
	 * @param data
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] data) {
        return (data == null || data.length == 0) 
        		? null : BitmapFactory.decodeByteArray(data, 0, data.length);
    }

	/**
	 * Drawable转为位图
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		return drawable == null ? null : ((BitmapDrawable)drawable).getBitmap();
	}

	/**
	 * 位图转为Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }
	
	/**
	 * Drawable转为字节数组
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToByte(Drawable drawable) {
        return bitmapToByte(drawableToBitmap(drawable));
    }
	
	/**
	 * 字节数组转为Drawable
	 * @param data
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] data) {
        return bitmapToDrawable(byteToBitmap(data));
    }

	/**
	 * @param bitmap      原图
	 * @return  缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap)
	{
		if(null == bitmap) {
			return  null;
		}
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();
		int edgeLength = Math.min(widthOrg, heightOrg);

		//从图中截取正中间的正方形部分。
		int xTopLeft = (widthOrg - edgeLength) / 2;
		int yTopLeft = (heightOrg - edgeLength) / 2;
		try{
			return Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
		} catch(Exception e){
			return null;
		}
	}

	/**
	 * 放大/缩小位图
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null || width<=0 || height<=0) return null;
		
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
	
	/**
	 * 放大/缩小位图
	 * @param bitmap
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight) {
        if (bitmap == null || scaleWidth<=0 || scaleHeight<=0) return null;
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
	
	
	/**
	* 旋转图片
	* @param bitmap
	* @param angle
	* @return
	*/
   	public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
	   if(bitmap==null) return null;
    	
	   Matrix matrix = new Matrix();
	   matrix.postRotate(angle);       
	   int width = bitmap.getWidth();
	   int height = bitmap.getHeight();
	   return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
   	}


	/**
	 * 获得圆角图片 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap roundBitmap(Bitmap bitmap, float roundPx) {
		if(bitmap == null || roundPx<=0) return null;
		
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

	/**
	 * 获得带倒影的图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap reflectBitmap(Bitmap bitmap) {
		if(bitmap == null) return null;
		
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
	
	
	/**
	 * 保存位图
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static void saveBitmap(Bitmap bitmap, String path) {
		saveBitmap(bitmap, path, null);
	}
	
	
	/**
	 * 保存位图
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static void saveBitmap(Bitmap bitmap, String path, CompressFormat format) {
		if(bitmap == null || StringUtil.isEmpty(path)) return ;
		FileOutputStream fileOut = null;
		
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			if(format == null) {
				if(path.endsWith(FileCst.SUFFIX_PNG)) {
					format = CompressFormat.PNG;
				} else if(path.endsWith(FileCst.SUFFIX_JPG) || path.endsWith(FileCst.SUFFIX_JPEG)){
					format = CompressFormat.JPEG;
				} else {
					format = CompressFormat.PNG;
				}
			}
			bitmap.compress(format, 100, byteOut);      
			byte[] buffer = byteOut.toByteArray();    
			
			fileOut = new FileOutputStream(path);
			fileOut.write(buffer, 0, buffer.length);
			fileOut.flush();
		}catch(Exception ex) {
		} finally {
			try {
				if(fileOut != null) fileOut.close();
			} catch(Exception ex) {
			}
		}
	}
	
	
   
   /**
    * 把一个View的对象转换成位图
    * @param view
    * @return
    */
    public static Bitmap viewToBitmap(View view) {
	    if(view == null) return null;
	   
	    view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
			        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

   /**
	 * 设置Drawable的灰度
	 * <pre>图标置灰   setDrawable(drawable, 0)</pre>
	 * @param drawable
	 * @param sat   0-1
	 * @return
	 */
	public static Drawable setSatDrawable(Drawable drawable, float sat) {
		drawable.mutate();  
		ColorMatrix cm = new ColorMatrix();  
		cm.setSaturation(sat);  
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);  
		drawable.setColorFilter(cf);
		return drawable;
	}
	
	/**
	 * 将位图分割成(xp * yp)块
	 * @param bitmap
	 * @param xp
	 * @param yp
	 * @return
	 */
	public static Bitmap[] splitBitmap(Bitmap bitmap, int xp, int yp) {
		if(bitmap ==null || xp<=0 || yp<=0) return null;
		
		Bitmap[] pieces = new Bitmap[xp * yp];   
		int pieceWidth = bitmap.getWidth() / xp;    
		int pieceHeight = bitmap.getHeight() / yp;    
		
		for (int i = 0; i < xp; i++) {    
			for (int j = 0; j < yp; j++) {    
				int xValue = j * pieceWidth;    
				int yValue = i * pieceHeight;    
				Bitmap piece = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceHeight);    
				pieces[i * xp + j] = piece;  
			}    
		}    
		
		return pieces;    
	}
	
	/**
	 * 生成水印图片
	 * @param src
	 * @param watermark
	 * @return
	 */
	public Bitmap watermarkBitmap( Bitmap src, Bitmap watermark ){
        if(src == null ) return null;
        if(watermark == null) return null;
 
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        
        Bitmap bitmap = Bitmap.createBitmap( w, h, Config.ARGB_8888 );
        Canvas cv = new Canvas( bitmap );
        cv.drawBitmap( src, 0, 0, null );
        cv.drawBitmap( watermark, w - ww + 5, h - wh + 5, null );
        cv.save( Canvas.ALL_SAVE_FLAG );
        cv.restore();
        return bitmap;
    }
	
	/**
	 * 获取指定图片的宽度
	 * @param context
	 * @param resId
	 * @return
	 */
    public static int getWidth(Context context, int resId) {
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeResource(context.getResources(), resId, options);
    	return options.outWidth;
    }
    
    /**
     * 获取指定图片的高度
     * @param context
     * @param resId
     * @return
     */
    public static int getHeight(Context context, int resId) {
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeResource(context.getResources(), resId, options);
    	return options.outHeight;
    }
    
    /**
     * 获取指定图片的类型
     * @param context
     * @param resId
     * @return
     */
    public static String getType(Context context, int resId) {
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeResource(context.getResources(), resId, options);
    	return options.outMimeType;
    }
    
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {
        // 设置inJustDecodeBounds=true，只获取图片信息
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //计算inSampleSize
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        
        options.inSampleSize = inSampleSize;
        
        // 解码图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    
    
    /**
     * get input stream from network by imageurl, you need to close inputStream yourself
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis) {
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if (readTimeOutMillis > 0) {
                con.setReadTimeout(readTimeOutMillis);
            }
            stream = con.getInputStream();
        } catch (MalformedURLException e) {
            closeInputStream(stream);
            throw new RuntimeException("MalformedURLException occurred. ", e);
        } catch (IOException e) {
            closeInputStream(stream);
            throw new RuntimeException("IOException occurred. ", e);
        }
        return stream;
    }

    /**
     * get drawable by imageUrl
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @return
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis) {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis);
        Drawable d = Drawable.createFromStream(stream, "src");
        closeInputStream(stream);
        return d;
    }

    /**
     * get Bitmap by imageUrl
     * 
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut);
        Bitmap b = BitmapFactory.decodeStream(stream);
        closeInputStream(stream);
        return b;
    }

    /**
     * close inputStream
     * 
     * @param s
     */
    private static void closeInputStream(InputStream s) {
        if (s == null) {
            return;
        }

        try {
            s.close();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }
    
    /** 
   	* @Title: getHeadBitmap 
   	* @Description:根据路径获取图片,获取制定宽高缩放的图片 
   	* @param @param path 图片路径
   	* @param @param width 宽
   	* @param @param height 高
   	* @param @return 设定文件 
   	* @return Bitmap 返回类型 
   	* @throws 
   	*/
    public static Bitmap getHeadBitmap(String path,int width,int height){
   		Options options = new Options();
   		options.inJustDecodeBounds = true;
   		BitmapFactory.decodeFile(path, options);
   		int scaleX = options.outWidth / width;
   		int scaleY = options.outHeight / height;
   		int scale = scaleX > scaleY ? scaleX : scaleY;
   		options.inJustDecodeBounds = false;
   		options.inSampleSize = scale;
   		return BitmapFactory.decodeFile(path, options);
   	}
    
    
    /**
     * bitmap转成byte[] 获得限制大小的图片JPEG数据 (用于头像)
     * 
     * @param bitmap
     *            图片
     * @param quality
     *            起始jpeg图片质量
     * @param limitSize
     *            限制的图片大小
     */
    public static byte[] getImageDataByLimitSize(Bitmap bitmap, int quality, int limitSize) {
    	if (bitmap == null || limitSize <= 0) { return null; }
    	
    	if (quality <= 0) { return null; }
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	bitmap.compress(CompressFormat.JPEG, quality, baos);
    	byte[] bytes = baos.toByteArray();
    	
    	if (bytes.length > limitSize)
    	{
    		bytes = null;
    		bytes = getImageDataByLimitSize(bitmap, quality - 10, limitSize);
    	}
    	
    	return bytes;
    }
   
    /** 
	* @Title: getBitmap 
	* @Description:根据图片数据字节数组获取图片,获取制定宽高缩放的图片
	* @param @param bytes 图片字节数组
	* @param @param width 宽
	* @param @param height 高
	* @param @return 设定文件 
	* @return Bitmap 返回类型 
	* @throws 
	* @date 2012-10-9 上午11:01:12 
	*/
	public static Bitmap getBitmap(byte[] bytes, int width, int height) {
		if(bytes == null) return null;
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		int scaleX = options.outWidth / width;
		int scaleY = options.outHeight / height;
		int scale = scaleX > scaleY ? scaleX : scaleY;
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}


	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
		LogUtil.d("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 1000f;//这里设置高度为800f
		float ww = 1000f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		//等比例缩放到指定宽高以内
		w = bitmap.getWidth();
		h = bitmap.getHeight();
		float scale = 1;
		if (w > ww || h > hh) {
			if ((float)w/h > ww/hh) {
				scale = ww/w;
			} else {
				scale = hh/h;
			}
		}
		bitmap = scaleBitmap(bitmap, scale, scale);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		int targetSize = 200 * 1024;
		while ( baos.toByteArray().length > targetSize) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap changeAlphaToWhite(Bitmap bitmap) {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawARGB(255, 255, 255, 255);
		canvas.drawBitmap(bitmap, 0, 0, new Paint());
		return newBitmap;
	}
}
