package com.example.chartView;

import com.example.test.R;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	//chartView mCharView;
	ChartTest mCharView;
	float[] testValues = { .2f, .6f, .9f, .4f, .5f, .1f, .6f, .5f, .8f, .4f };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
        mCharView= (ChartTest) findViewById(R.id.test_char);
        mCharView.setValues(testValues,1.0f);
	}
}
