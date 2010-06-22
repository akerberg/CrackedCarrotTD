#include <jni.h>
#include <android/log.h>
#include <malloc.h>
#include <GLES/glplatform.h>
#include <GLES/gl.h>
#include <GLES/glext.h>

enum spriteType {
	BACKGROUND = 0,
	EFFECT     = 1,
	CREATURE   = 2,
	SHOT       = 3,
	OVERLAY    = 4,
	TOWER      = 5,
	UI         = 6
};

typedef struct {
    //jobject object;
    jfieldID dead;
} crExtensions;

typedef struct {
    //GLuint  textureName;
    GLuint  nFrames;
    GLuint* textureBufferNames;
} textureData;

typedef struct {
	
	enum spriteType type;
	int subType;
	
    jobject object;
    
    jfieldID width, height, scale;
    jfieldID x, y, z;
	jfieldID draw;
    jfieldID textureName;
	jfieldID r, g, b, opacity;
	jfieldID cFrame;

	crExtensions* crExtens;
	
	GLushort 	indexCount;
	GLuint* bufferName;


} GLSprite;

typedef struct {
    GLSprite screen;
    GLfloat r,g,b;
} loadingScreen;

//pointers to arrays of sprites and the length of these arrays.

int noOfSprites[7];
GLSprite* renderSprites[7];

//The loadingscreen
loadingScreen lScreen;

//Array with pointers to textuerData structures for saveing all data related
//to a certain textures
textureData* texData;
int texDataLength;
//GLuint* textureNameWorkspace;
//GLuint* cropWorkspace;

enum bufferTag {
	INDEX_OBJECT = 0,
	VERT_OBJECT = 1
};

void initHwBuffers(JNIEnv* env, GLSprite* sprite);

void Java_com_crackedcarrot_NativeRender_nativeAllocTextureBuffers(JNIEnv* env, jobject thiz, jint length);
void Java_com_crackedcarrot_NativeRender_nativeSetTextureBuffer(JNIEnv* env, jobject thiz, jobject textureData);

void Java_com_crackedcarrot_NativeRender_nativeResize(JNIEnv*  env, jobject  thiz, jint w, jint h);
void Java_com_crackedcarrot_NativeRender_nativeDrawFrame(JNIEnv*  env, jobject thiz);
void Java_com_crackedcarrot_NativeRender_nativeSurfaceCreated(JNIEnv*  env, jobject thiz);
void Java_com_crackedcarrot_NativeRender_nativeFreeSprites(JNIEnv* env, jobject thiz);
void drawSprite(JNIEnv* env, GLSprite* sprite);


