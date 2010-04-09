#include "render.h"

//#define emulator

void Java_com_crackedcarrot_NativeRender_nativeDataPoolSize(JNIEnv* env,
															jobject thiz, 
															jint type, 
															jint size){
                                                                  
    noOfSprites[type] = size;
    renderSprites[type] = malloc(sizeof(GLSprite) * noOfSprites[type]);
		//textureNameWorkspace = malloc(sizeof(GLuint) * 1);
		//cropWorkspace = malloc(sizeof(GLuint) * 1);
	
    __android_log_print(ANDROID_LOG_DEBUG, 
						"NATIVE ALLOC",
						"Allocating memory pool for Sprites, Type %d of size %d ", 
						type, noOfSprites[type]);
}

void Java_com_crackedcarrot_NativeRender_nativeAlloc(JNIEnv*  env, 
													 jobject thiz, 
													 jint spriteNO, 
													 jobject sprite){
	
													
	//Get the class and read the type info
	jclass class = (*env)->GetObjectClass(env, sprite);
	jfieldID id = (*env)->GetFieldID(env, class, "type", "I");
	jint type = (*env)->GetIntField(env, sprite, id);
	
	//Use type to figure out what element to manipulate
	GLSprite* thisSprite = &renderSprites[type][spriteNO];
	//Set a variable, dont cache reference as we do with the rest of the members of the
	//sprite class, since the type is imutable.
	thisSprite->type = type;
	
	id = (*env)->GetFieldID(env, class, "subType", "I");
	jint subType = (*env)->GetIntField(env, sprite, id);
	thisSprite->subType = subType;
	
	//Cache reference to this object
	thisSprite->object = (*env)->NewGlobalRef(env,sprite);
	
		//cache the x,y,z pos ID
	id = (*env)->GetFieldID(env, class, "x", "F");
	thisSprite->x = id;
	id = (*env)->GetFieldID(env, class, "y", "F");
	thisSprite->y = id;
	id = (*env)->GetFieldID(env, class, "z", "F");
	thisSprite->z = id;
	
		//cache the width/height IDs
	id = (*env)->GetFieldID(env, class, "width", "F");
	thisSprite->width = id;
	id = (*env)->GetFieldID(env, class, "height", "F");
	thisSprite->height = id;
	id = (*env)->GetFieldID(env, class, "scale", "F");
	thisSprite->scale = id;
	
	id = (*env)->GetFieldID(env, class, "draw", "Z");
	thisSprite->draw = id;
	
	id = (*env)->GetFieldID(env, class, "r", "F");
	thisSprite->r = id;
	id = (*env)->GetFieldID(env, class, "g", "F");
	thisSprite->g = id;
	id = (*env)->GetFieldID(env, class, "b", "F");
	thisSprite->b = id;
	id = (*env)->GetFieldID(env, class, "opacity", "F");
	thisSprite->opacity = id;
	
	id = (*env)->GetFieldID(env, class, "nFrames", "I");
	thisSprite->nFrames = id;
	id = (*env)->GetFieldID(env, class, "cFrame", "I");
	thisSprite->cFrame = id;
	
		//cache TextureName
	id = (*env)->GetFieldID(env, class, "mTextureName", "I");
	thisSprite->textureName = id;
	
	thisSprite->bufferName[0] = 0; 
	thisSprite->bufferName[1] = 0;
	thisSprite->textureBufferNames = NULL;
	
	//If this is not the first sprite of its type and its not an animation
	//We can just use the same VBOs as the last sprite.
	GLSprite* last = NULL;
	if(spriteNO != 0){
		last = &renderSprites[type][spriteNO-1];
	}
	if(last != NULL && last->subType == thisSprite->subType){

	    thisSprite->vertBuffer = last->vertBuffer;
	    thisSprite->indexBuffer = last->indexBuffer;
	    thisSprite->textureCoordBuffers = last->textureCoordBuffers;
	    
		thisSprite->bufferName[0] = last->bufferName[0]; 
		thisSprite->bufferName[1] = last->bufferName[1];
		thisSprite->textureBufferNames = last->textureBufferNames;

		__android_log_print(ANDROID_LOG_DEBUG, 
						"NATIVE ALLOC",
						"Sprite No: %d of Type: %d and subType: %d .  Can share data with the previous sprite",
						 spriteNO, type, subType);
		__android_log_print(ANDROID_LOG_DEBUG, 
						"NATIVE ALLOC",
						"It has been assigned the buffers: %d, %d and %d",
						 thisSprite->bufferName[0], thisSprite->bufferName[1], thisSprite->textureBufferNames[0]);

	}
	else{
	    __android_log_print(ANDROID_LOG_DEBUG, 
		                "NATIVE ALLOC", 
		                "Sprite No: %d of Type: %d and subType: %d .   Needs new buffers.",
		                spriteNO, type, subType);

		initHwBuffers(env, thisSprite);

	}
}

void initHwBuffers(JNIEnv* env, GLSprite* sprite){
	GLsizeiptr vertBufSize;
	GLsizeiptr textCoordBufSize;
	GLsizeiptr indexBufSize;

	
	vertBufSize = sizeof(GLfloat) * 4 * 3;
	indexBufSize = sizeof(GLuint) * 6;


	sprite->vertBuffer = malloc(vertBufSize);
	sprite->indexBuffer = malloc(indexBufSize);

	sprite->indexCount = 6;

	GLfloat* vertBuffer  = sprite->vertBuffer;
    GLfloat* indexBuffer = sprite->indexBuffer;
	
	//This be the vertex order for our quad, its totaly square man.
	indexBuffer[0] = 0;
	indexBuffer[1] = 1;
	indexBuffer[2] = 2;
	indexBuffer[3] = 1;
	indexBuffer[4] = 2;
	indexBuffer[5] = 3;
		
	GLfloat width = (*env)->GetFloatField(env, sprite->object, sprite->width);
	GLfloat height = (*env)->GetFloatField(env, sprite->object, sprite->height);
		
	//VERT1
	vertBuffer[0] = 0.0;
	vertBuffer[1] = 0.0;
	vertBuffer[2] = 0.0;	
	//VERT2
	vertBuffer[3] = width;
	vertBuffer[4] = 0.0;
	vertBuffer[5] = 0.0;
	//VERT3
	vertBuffer[6] = 0.0;
	vertBuffer[7] = height;
	vertBuffer[8] = 0.0;
	//VERT4
	vertBuffer[9] = width;
	vertBuffer[10] = height;
	vertBuffer[11] = 0.0;
	//WOOO I CAN HAS QUAD!
	
	glGenBuffers(2, sprite->bufferName);
	//__android_log_print(ANDROID_LOG_DEBUG, "HWBUFFER ALLOC", "GenBuffer retured error: %d", glGetError());
	glBindBuffer(GL_ARRAY_BUFFER, sprite->bufferName[VERT_OBJECT]);
	glBufferData(GL_ARRAY_BUFFER, vertBufSize, vertBuffer, GL_STATIC_DRAW);
	
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, sprite->bufferName[INDEX_OBJECT]);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBufSize, indexBuffer, GL_STATIC_DRAW);
	
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	
	//Texture Coords
	
	int frames = (*env)->GetIntField(env, sprite->object, sprite->nFrames);

	textCoordBufSize = sizeof(GLfloat) * 4 * 2;
	sprite->textureCoordBuffers = malloc(textCoordBufSize * frames);
	
	sprite->textureBufferNames = malloc(frames * sizeof(GLuint));
	GLfloat* textureCoordBuffer = sprite->textureCoordBuffers;
	glGenBuffers(frames, sprite->textureBufferNames);
	
	float texFraction = 1.0 / frames;
	float startFraction = 0.0;
	float endFraction;
	int i;
	int offset = 0;
	for(i = 0; i < frames; i++){
		endFraction = startFraction + texFraction;
		__android_log_print(ANDROID_LOG_DEBUG, 
		                "HWBUFFER ALLOC", 
		                "Allocating memcoords set no: %d from %f to %f", 
		                i ,startFraction, endFraction );
		textureCoordBuffer[offset + 0] = startFraction; 	textureCoordBuffer[offset + 1] = 1.0;
		textureCoordBuffer[offset + 2] = endFraction; 	    textureCoordBuffer[offset + 3] = 1.0;
		textureCoordBuffer[offset + 4] = startFraction; 	textureCoordBuffer[offset + 5] = 0.0;
		textureCoordBuffer[offset + 6] = endFraction; 	    textureCoordBuffer[offset + 7] = 0.0;
		glBindBuffer(GL_ARRAY_BUFFER, sprite->textureBufferNames[i]);
		glBufferData(GL_ARRAY_BUFFER, textCoordBufSize, textureCoordBuffer+offset,GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		startFraction = endFraction;
		offset = offset + 8;
	}
	__android_log_print(ANDROID_LOG_DEBUG, 
		                "HWBUFFER ALLOC", 
		                "Sprite has been assigned the new buffers: %d, %d and %d", 
		                sprite->bufferName[INDEX_OBJECT] ,sprite->bufferName[VERT_OBJECT], sprite->textureBufferNames[0] );

}

void Java_com_crackedcarrot_NativeRender_nativeFreeSprites(JNIEnv* env){
	GLSprite* currSprt;
	int spritesToFree;
	int i;
	int j;
	
	for(j = 0; j < 6; j++){
		spritesToFree = noOfSprites[j];
		for(i = 0; i < spritesToFree; i++){
			currSprt = &renderSprites[j][i];
			__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", "Freeing sprite %d:%d", j,i);
		
			#ifndef emulator
			glDeleteBuffers(2, currSprt->bufferName);
			glDeleteBuffers((*env)->GetIntField(env, currSprt->object, currSprt->nFrames),
			 				currSprt->textureBufferNames);
			#endif
			(*env)->DeleteGlobalRef(env, currSprt->object);
		}
	
		free(renderSprites[j]);
		noOfSprites[j] = 0;
	}
}

void Java_com_crackedcarrot_NativeRender_nativeFreeTex(JNIEnv* env, jobject thiz, jint textureName){
	glDeleteTextures(1, &textureName);
}
