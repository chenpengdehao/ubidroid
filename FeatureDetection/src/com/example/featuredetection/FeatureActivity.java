package com.example.featuredetection;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.pm.*;

public class FeatureActivity extends Activity 
{
	ListView list;
	TextView tv1;
	ArrayAdapter<String> mListViewArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//list = (ListView) findViewById(R.id.ListView1);
		tv1 = (TextView) findViewById(R.id.TV1);
		//list.setAdapter(mListViewArrayAdapter);
		Context context = getApplicationContext();
	    PackageManager pm = context.getPackageManager();
	    FeatureInfo[] featureInfoList = pm.getSystemAvailableFeatures();
	    if(featureInfoList == null)
	    {
	          Log.e("ERROR","No feature is available");
	    }
	    else
	    {
	          //send feature list to List view and Log  
	          for (int i=0; i<featureInfoList.length; i++)
	          {
	                 String featureName = featureInfoList[i].name;
	                 Log.d("DEBUG", "Feature available: "+ featureName);
	                 tv1.append("\n" + featureName);
	                 //mListViewArrayAdapter.add(featureName);
	          }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
