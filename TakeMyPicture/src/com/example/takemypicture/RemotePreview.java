package com.example.takemypicture;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

public class RemotePreview extends VideoView {
	MediaController mMediaController;
	RemotePreview(Context context) {
		super(context);
		
		mMediaController = new MediaController(context);
	}

	public void playvideo(Uri uri)
	{
		this.setVideoURI(uri);
		this.setMediaController(mMediaController);
		this.start();
	}
	
}
