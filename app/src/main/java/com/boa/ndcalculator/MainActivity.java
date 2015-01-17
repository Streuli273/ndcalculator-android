package com.boa.ndcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity {

	String[] shutters = {
			"1/8000",
			"1/4000",
			"1/2000",
			"1/1000",
			"1/500",
			"1/400",
			"1/250",
			"1/200",
			"1/160",
			"1/125",
			"1/100",
			"1/80",
			"1/60",
			"1/50",
			"1/40",
			"1/30",
			"1/25",
			"1/20",
			"1/15",
			"1/8",
			"1/4",
			"0\"5",
			"0\"6",
			"0\"8",
			"1\"",
			"2\"",
			"2.5\"",
			"4\"",
			"5\"",
			"6\"",
			"8\"",
			"10\"",
			"15\"",
			"20\"",
			"25\"",
			"30\"",
	};

	String[] densitys = {
			"1 Stop",
			"2 Stops",
			"3 Stops",
			"4 Stops",
			"5 Stops",
			"6 Stops",
			"7 Stops",
			"8 Stops",
			"9 Stops",
			"10 Stops",
	};

	private double shutterVal;
	private double densityVal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView textView = (TextView) findViewById(R.id.text_shutter_short);
		final TextView textView1 = (TextView) findViewById(R.id.text_shutter_long);

		final NumberPicker ndPicker = (NumberPicker) findViewById(R.id.picker_nd);
		ndPicker.setMaxValue(densitys.length - 1);
		ndPicker.setMinValue(0);
		ndPicker.setDisplayedValues(densitys);
		//ndPicker.setValue(1);
		//ndPicker.setWrapSelectorWheel(false);

		final NumberPicker shutterPicker = (NumberPicker) findViewById(R.id.picker_shutter);
		shutterPicker.setMaxValue(shutters.length - 1);
		shutterPicker.setMinValue(0);
		shutterPicker.setDisplayedValues(shutters);
		//shutterPicker.setValue(1);
		//shutterPicker.setWrapSelectorWheel(false);

		ndPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal == 0) {
					densityVal = parseArray(newVal, densitys[newVal], 'x');
				} else {
					densityVal = parseArray(newVal, densitys[newVal], 'd');
				}

				/*Log.d("ND PICKER", "newVal = " + newVal + " oldVal = " + oldVal);
				Log.d("ND PICKER", "densityVal = " + densityVal);*/

				doCalculation(shutterPicker, ndPicker, textView, textView1);
			}
		});

		shutterPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal <= 20) {
					shutterVal = parseArray(newVal, shutters[newVal], 's');
				} else {
					if (newVal >= 21 && newVal <= 23) {
						shutterVal = parseArray(newVal, shutters[newVal], 'u');
					} else {
						shutterVal = parseArray(newVal, shutters[newVal], 't');
					}
				}

				/*Log.d("SHUTTER PICKER", "newVal = " + newVal + " oldVal = " + oldVal);
				Log.d("SHUTTER PICKER", "shutterVal = " + shutterVal);
				Log.d("SHUTTER PICKER", "shutters[newVal] = " + shutters[newVal]);*/

				doCalculation(shutterPicker, ndPicker, textView, textView1);
			}
		});

	}


	/** Do something with the numbers... **/
	public void doCalculation(NumberPicker shutterPicker, NumberPicker ndPicker, TextView textView, TextView textView1) {

		double finalShutter = 0;

		int densityValInt = (int)densityVal;

		switch (densityValInt) {
			case 1:
				finalShutter = shutterVal * 2;
				break;
			case 2:
				finalShutter = shutterVal * 4;
				break;
			case 3:
				finalShutter = shutterVal * 8;
				break;
			case 4:
				finalShutter = shutterVal * 16;
				break;
			case 5:
				finalShutter = shutterVal * 32;
				break;
			case 6:
				finalShutter = shutterVal * 64;
				break;
			case 7:
				finalShutter = shutterVal * 125;
				break;
			case 8:
				finalShutter = shutterVal * 250;
				break;
			case 9:
				finalShutter = shutterVal * 500;
				break;
			case 10:
				finalShutter = shutterVal * 1000;
				break;
		}

		finalShutter = Math.floor(finalShutter);

		DecimalFormat df = new DecimalFormat("###.#");

		String finalShutterStr = String.valueOf(df.format(finalShutter));
		String finalShutterStrLong = "";

		if (finalShutter >= 0.5) {
			if (finalShutterStr.contains(".")) {
				finalShutterStr = finalShutterStr.replace(".", "\"");
				finalShutterStr = finalShutterStr + "\n\n";
			} else {
				finalShutterStr += "\"";
				finalShutterStr = finalShutterStr + "\n\n";
			}

			if (finalShutter > 60) {
				int hours = (int) finalShutter / 3600;
				int minutes = ((int) finalShutter % 3600) / 60;
				int seconds = (int) finalShutter % 60;

				finalShutterStrLong = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);
				finalShutterStr = finalShutterStr.replace(".0", "\"");
			}
		} else {
			String aString = Double.toString(finalShutter);
			String[] fraction = aString.split("\\.");

			int denominator = (int)Math.pow(10, fraction[1].length());
			int numerator = Integer.parseInt(fraction[0] + "" + fraction[1]);

			if (numerator != 0) {
				denominator /= numerator;
				denominator = Math.round(denominator);
				numerator = 1;
				finalShutterStr = numerator + "/" + denominator + "\"\n\n";
			} else {
				finalShutterStr = shutters[shutterPicker.getValue() + ndPicker.getValue()] + "\n\n";
			}

		}

		textView.setText(finalShutterStr);
		textView1.setText(finalShutterStrLong);

	}

	/** Gather usable data from arrays from inout methods **/
	public double parseArray(int valPos, String value, char valueType) {
		double finalVal = 0;

		if (valueType == 's') {
			String splitChar = "[/]+";
			String[] splitted = value.split(splitChar);
			finalVal = (double)Integer.parseInt(splitted[0]) / (double)Integer.parseInt(splitted[1]);
		} else {
			if (valueType == 't') {
				value = value.replace("\"", "");
				finalVal = Double.parseDouble(value);
			} else {
				if (valueType == 'u') {
					value = value.replace("\"", ".");
					finalVal = Double.parseDouble(value);
				} else {
					if (valueType == 'd') {
						value = value.replace(" Stops", "");
						finalVal = Double.parseDouble(value);
					} else {
						if (valueType == 'x') {
							value = value.replace(" Stop", "");
							finalVal = Double.parseDouble(value);
						}
					}
				}
			}
		}

		return finalVal;
	}

	public void openHelp() {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_help) {
			openHelp();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
