package com.crackedcarrot;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

public class NativeRender implements GLSurfaceView.Renderer {
	
	public	static final int BACKGROUND = 0;
	public 	static final int CREATURE	= 1;
	public	static final int TOWER		= 2;
	public 	static final int SHOT		= 3;

	private static native void nativeAlloc(int n, Sprite s);
	private static native void nativeDataPoolSize(int size);
    private static native void nativeResize(int w, int h);
    private static native void nativeDrawFrame();
    private static native void nativeSurfaceCreated();
    private static native void nativeFreeSprites();
    private static native void nativeFreeTex(int i);
    
    
    private Semaphore surfaceReady = new Semaphore(0);
    
	private Sprite[][] sprites = new Sprite[4][];
	private Sprite[] renderList;
	
	private int[] mCropWorkspace;
	private int[] mTextureNameWorkspace;
	//public boolean mUseHardwareBuffers;
	private Context mContext;
	private GLSurfaceView view;
	private GL10 glContext;
	private static BitmapFactory.Options sBitmapOptions
    = new BitmapFactory.Options();
	
	private HashMap<Integer,Integer> textureMap = new HashMap<Integer,Integer>();
	
	public NativeRender(Context context, GLSurfaceView view) {
        // Pre-allocate and store these objects so we can use them at runtime
        // without allocating memory mid-frame.
        mTextureNameWorkspace = new int[1];
        mCropWorkspace = new int[4];
        
        // Set our bitmaps to 16-bit, 565 format.
        sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        
		mContext = context;
		this.view = view;
		System.loadLibrary("render");
	}

	public void onDrawFrame(GL10 gl) {
		nativeDrawFrame();
	}

	//@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		nativeResize(width, height);
	}
	
	//@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if(renderList != null){
			for(int i = 0; i < renderList.length; i++){
	            // If we are using hardware buffers and the screen lost context
	            // then the buffer indexes that we recorded previously are now
	            // invalid.  Forget them here and recreate them below.
	            
	            // Load our texture and set its texture name on all sprites.
	            
	            int lastTextureId = -1;
	            textureMap.clear();
	            for (int x = 0; x < renderList.length; x++) {
	                int resource = renderList[i].getResourceId();
	                if (!textureMap.containsKey(resource)) {
						lastTextureId = loadBitmap(mContext, gl, resource);
						textureMap.put(resource, lastTextureId);
	                }
	                renderList[i].setTextureName(lastTextureId);
	            }
	        }
		}
		glContext = gl;
		nativeSurfaceCreated();
		surfaceReady.release();
	}
	
	public void finalizeSprites() {
		
		try {
			surfaceReady.acquire();
			int listSize = 0;
			for(int i = 0; i < sprites.length; i++){
				if(sprites[i] != null)
				listSize += sprites[i].length;
			}
			
	        renderList = new Sprite[listSize];
	        
	        for(int i = 0, j = 0; i < sprites.length; i++){
	        	if(sprites[i] != null){
	        		for(int k = 0; k < sprites[i].length; k++){
	        			renderList[j] = sprites[i][k];
	        			j++;
	        		}
	        	}
	        }
	        
	        //This needs to run on the render Thread to get access to the glContext.
        	view.queueEvent(new Runnable(){
			//@Override
			public void run() {
		        nativeDataPoolSize(renderList.length);
	            int lastTextureId = -1;
		        for(int i = 0; i < renderList.length; i++){
					// TODO Auto-generated method stub
					nativeAlloc(i, renderList[i]);
		        	//Try to load textures
					int resource = renderList[i].getResourceId();
	                if (!textureMap.containsKey(resource)) {
						lastTextureId = loadBitmap(mContext, glContext, resource);
						textureMap.put(resource, lastTextureId);
	                }
	                renderList[i].setTextureName(lastTextureId);
				}
		    }
        	});
        	//End of code that needs to run in the render thread.
	        surfaceReady.release();
	        
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSprites(Sprite[] spriteArray, int type){
		sprites[type] = spriteArray;
	}
	
	/**
	 * Frees all allocated sprite data in the render.
	 * this means nothing is left on the screen after this.
	 * 
	 * However All textures remain in their buffers. And can be reused.
	 */
	public void freeSprites(){
		try {
			surfaceReady.acquire();
			view.queueEvent(new Runnable(){
				//@Override
				public void run() {
			        nativeFreeSprites();		    }
	        });
			this.renderList = null;
			for (int i=0; i < sprites.length; i++)
				this.sprites[i] = null;
			
			surfaceReady.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * This loads a texture into the render for drawing,
	 * needs to be done before use. If the texture is already
	 * loaded it just returns the texture name without loading
	 * again.
	 * 
	 * Takes the resource id of the sprite and returns the
	 * internal texture name;
	 * 
	 * 
	 * @param resourceId
	 * @return textureName
	 */
	public void loadTexture(int rId){
		try {
			surfaceReady.acquire();
			final int resourceId = rId;
			view.queueEvent(new Runnable(){
				//@Override
				public void run() {
					int lastTextureId = 0;
					if (!textureMap.containsKey(resourceId)) {
						lastTextureId = loadBitmap(mContext, glContext, resourceId);
						textureMap.put(resourceId, lastTextureId);
		            }
				}
			});
			surfaceReady.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This frees the texture specified by resource id
	 * from the buffers.
	 * 
	 * Use full when you want to reuse most of the allocated
	 * textures for a new wave and want to avoid reloading all of them.
	 * 
	 * @param resourceId
	 */
	public void freeTexture(int resourceId){
		final int rId = resourceId;
		try {
			surfaceReady.acquire();
			view.queueEvent(new Runnable(){
				//@Override
				public void run() {
					Integer i = textureMap.get(rId);
			        nativeFreeTex(i.intValue());		    }
	        });
			surfaceReady.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Free all textures from buffers.
	 * To draw anything after this new textures must be loaded.
	 */
	@SuppressWarnings("unchecked")
	public void freeAllTextures(){
		try {
			
			final HashMap<Integer,Integer> map = (HashMap<Integer, Integer>) textureMap.clone();
			
			textureMap.clear();
			
			surfaceReady.acquire();
			view.queueEvent(new Runnable(){
				//@Override
				public void run() {
					
					Iterator<Integer> it = map.values().iterator();
					while(it.hasNext()){
				        nativeFreeTex(it.next().intValue());
					}
				}
			});
			
			surfaceReady.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Tackes the resourceId of a sprite and 
	 * returns the texture name if it exist.
	 * @param resourceId
	 * @return
	 */
	public int getTextureName(int resourceId){
		return textureMap.get(resourceId);
	}
	
	private int loadBitmap(Context context, GL10 gl, int resourceId) {
        int textureName = -1;
        if (context != null && gl != null) {
            gl.glGenTextures(1, mTextureNameWorkspace, 0);

            textureName = mTextureNameWorkspace[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

            InputStream is = context.getResources().openRawResource(resourceId);
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            mCropWorkspace[0] = 0;
            mCropWorkspace[1] = bitmap.getHeight();
            mCropWorkspace[2] = bitmap.getWidth();
            mCropWorkspace[3] = -bitmap.getHeight();
            
            bitmap.recycle();

            ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
                    GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);

            
            int error = gl.glGetError();
            if (error != GL10.GL_NO_ERROR) {
                Log.e("SpriteMethodTest", "Texture Load GLError: " + error);
            }
        
        }
        Log.d("JAVA_LOADTEXTURE", "Loading texture, id is: " + textureName);
        return textureName;
    }
}
