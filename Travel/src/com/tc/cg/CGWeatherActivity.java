//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.logic.BaseWeather;
import com.tc.logic.CurrentWeather;
import com.tc.logic.ResourceAdapter;
import com.tc.logic.Weather;
import com.tc.net.WeatherUtil;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class CGWeatherActivity extends CGBaseActivity {
	private class CGWeatherAsy extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			String s = null;
			try {
				BaseWeather baseweather = WeatherUtil.getWeatherByCity(CG_DATA.CG_CNAME);
				//Log.d("test", (new StringBuilder()).append("baseWeather").append(baseweather).toString());
				ArrayList<Weather> arraylist = baseweather.getWeatherList();
				ResourceAdapter.imgs = new int[arraylist.size()];
				ResourceAdapter.weather_icon = new int[arraylist.size()];
				boolean flag = WeatherUtil.isNight();
				//Log.d("test", (new StringBuilder()).append("isNight=").append(flag).toString());

				for (int i = 0; i < arraylist.size(); i++) {
					if(i==0)
					   ResourceAdapter.imgs[i] = WeatherUtil.getImageName(arraylist.get(i).getCurrently_img(), flag);
					else 
					   ResourceAdapter.imgs[i] = WeatherUtil.getImageName(arraylist.get(i).getCurrently_img(), false);
					ResourceAdapter.weather_icon[i] = WeatherUtil.getWeatherIcon(arraylist.get(i).getCurrently_img());
				}

				//ResourceAdapter.current_img = WeatherUtil.getImageName(
				//		baseweather.getCurrentWeatherList().get(0).getCurrent_icon(), flag);
				//ResourceAdapter.current_icon = WeatherUtil.getWeatherIcon( baseweather.getCurrentWeatherList().get(0)
				//		.getCurrent_icon());
				ResourceAdapter.setBaseWeather(baseweather);
				ResourceAdapter.setMessageCode(3);

			} catch (IllegalArgumentException e) {
				ResourceAdapter.setMessageCode(2);
				s = "\u9519\u8BEF";
			} catch (IOException e) {
				ResourceAdapter.setMessageCode(1);
				s = "\u9519\u8BEF";
			} catch (ParserConfigurationException e) {
				ResourceAdapter.setMessageCode(1);
				s = "\u9519\u8BEF";
			} catch (SAXException e) {
				ResourceAdapter.setMessageCode(1);
				s = "\u9519\u8BEF";
			}
			return s;

		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
		    if (adapter.getWeatherByIndex(0) == null) {
				defaultView();
				returnButton.setOnClickListener(new android.view.View.OnClickListener() {

					public void onClick(View view) {
						finish();
					}

				});
//				today_weahter.setOnClickListener(new android.view.View.OnClickListener() {
//
//					public void onClick(View view) {
//						defaultView();
//					}
//
//				});
//				nextone_weather.setOnClickListener(new android.view.View.OnClickListener() {
//
//					public void onClick(View view) {
//						defaultView();
//					}
//
//				});
//				nexttwo_weather.setOnClickListener(new android.view.View.OnClickListener() {
//
//					public void onClick(View view) {
//						defaultView();
//					}
//
//				});
				// nextthree_weather.setOnClickListener(new
				// android.view.View.OnClickListener() {
				//
				// public void onClick(View view) {
				// defaultView();
				// }
				//
				// });
			} else {
				initView(0,true);
				
			}
		}

	}

	public CGWeatherActivity() {
		adapter = null;
	}

	public void action() {
		String as[] = CG_DATA.CG_WEATHER.split("split");
		if (as != null && as.length != 0) { // goto _L2; else goto _L1
			String s = as[0];
			//Log.d("test", (new StringBuilder()).append("weather_sunny=").append(s).toString());
			String s1 = as[1];
			if (hint_info.getText().equals("\u6674") || hint_info.getText().equals("\u4EE5\u6674\u4E3A\u4E3B"))
				action.setText(s);
			else if (hint_info.getText() != null)
				action.setText(s1);
		} else {
			action.setText("\u65E0\u6570\u636E");
		}
	}
	
    private class WeatherClickListener implements android.view.View.OnClickListener{
    	private int position;
    	public WeatherClickListener(int pos){
    		this.position=pos;
    	}
    	public void onClick(View view) {
    		initView(position,false);
    	}
    }
	
	public void click() {
		returnButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				finish();
			}

		});
		for(int i=0;i<forecastsLayout.length;i++){
			forecastsLayout[i].setOnClickListener(new WeatherClickListener(i));
		}
		
	
	}

	public void clothing(int i) {
		String s = (new StringBuilder()).append(getString(0x7f0800d9)).append(" ,").append(getString(0x7f0800da)).append(" ,")
				.append(getString(0x7f0800db)).toString();
		String s1 = (new StringBuilder()).append(getString(0x7f0800dc)).append(",").append(getString(0x7f0800dd)).append(" ,")
				.append(getString(0x7f0800de)).toString();
		String s2 = (new StringBuilder()).append(getString(0x7f0800e1)).append(" ,").append(getString(0x7f0800e2)).append(" ,")
				.append(getString(0x7f0800e3)).toString();
		String s3 = (new StringBuilder()).append(getString(0x7f0800e4)).append(" ,").append(getString(0x7f0800e2)).append(" ,")
				.append(getString(0x7f0800e5)).toString();
		String s4 = (new StringBuilder()).append(getString(0x7f0800e7)).append(" ,").append(getString(0x7f0800e8)).append(" ,")
				.append(getString(0x7f0800e9)).append(" ,").append(getString(0x7f0800ea)).toString();
		if (i == 0x1869f)
			clothing.setText("\u65E0\u6570\u636E");
		else if (i > 29)
			clothing.setText(s);
		else if (i > 22)
			clothing.setText(s1);
		else if (i > 16)
			clothing.setText(s2);
		else if (i > 10)
			clothing.setText(s3);
		else
			clothing.setText(s4);
		if (!hint_info.getText().equals("\u6674") && !hint_info.getText().equals("\u4EE5\u6674\u4E3A\u4E3B")) { // goto
																												// _L2;
																												// else
																												// goto
																												// _L1
			if (hint_info.getText().equals("\u96F6\u661F\u96F7\u96E8") || hint_info.getText().equals("\u96F7\u9635\u96E8")
					|| hint_info.getText().equals("\u5C40\u90E8\u96F7\u96E8"))
				clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800ec)).toString());
			else if (hint_info.getText().equals("\u51B0\u96F9"))
				clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800ed)).toString());
			else if (hint_info.getText().equals("\u96E8\u5939\u96EA") || hint_info.getText().equals("\u5C0F\u96EA")
					|| hint_info.getText().equals("\u4E2D\u96EA") || hint_info.getText().equals("\u5927\u96EA"))
				clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800ee)).toString());
			else if (hint_info.getText().equals("\u66B4\u98CE\u96E8") || hint_info.getText().equals("\u591A\u98CE\u5929\u6C14")
					|| hint_info.getText().equals("\u53EF\u80FD\u6709\u66B4\u98CE\u96E8"))
				clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800ef)).toString());
			else if (hint_info.getText().equals("\u53EF\u80FD\u6709\u96E8") || hint_info.getText().equals("\u5C0F\u96E8")
					|| hint_info.getText().equals("\u96F6\u661F\u9635\u96E8"))
				clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800f0)).toString());
			else if (hint_info.getText().equals("\u96E8") || hint_info.getText().equals("\u9635\u96E8"))
				clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800f0)).append(" ,")
						.append(getString(0x7f0800f1)).toString());

		} else {
			clothing.setText((new StringBuilder()).append(clothing.getText()).append(" ,").append(getString(0x7f0800eb)).toString());
		}

	}

	public void defaultView() {
		imageview.setImageResource(0x7f02004c);
		week_info.setText("");
		date_info.setText("");
		current_temperature.setText("0\260");
		current_temperature.setTextSize(65F);
		hint_info.setText("");
		temperature_view.setText("\u6E29\u5EA6\uFF1A0\260C/0\260C");
		humidity.setText(" \u6E7F\u5EA6\uFF1A\u65E0\u6570\u636E");
		wind.setText("\u98CE\u5411\uFF1A\u65E0\u6570\u636E");
		clothing(0);
		action.setText("\u65E0\u6570\u636E");

		for(int i=0;i<forecastsDay.length;i++){
			forecastsImages[i].setImageResource(R.drawable.noda_icon);
			if(i!=0) //today week need not change
			forecastsDay[i].setText("");
			forecastsTemprature[i].setText("0\260C/0\260C");
		}
		
		// nextthree_temperature.setText("0\260C/0\260C");
		sunproof.setText("\u9632\u6652\u6307\u6570\uFF1A\u4E0D\u9700\u8981");
		update_date.setText("\u5DF2\u66F4\u65B0");
	}
    
	public void initView(int position,boolean init){
		ArrayList<Weather> weathers=ResourceAdapter.getBaseWeather().getWeatherList();
		if(position<weathers.size()){
			Weather weather=weathers.get(position);
			week_info.setText(weather.getCurrently_week());
			date_info.setText(weather.getCurrently_date());
			StringBuilder stringbuilder = new StringBuilder();
			stringbuilder.append("\u6E29\u5EA6\uFF1A").append(weather.getTemperature_min()).append("\260").append("C").append(" / ")
					.append(weather.getTemperature_max()).append("\260").append("C");
			temperature_view.setText(stringbuilder);
			stringbuilder.delete(0, stringbuilder.length());
			
			if(weather instanceof CurrentWeather){
				CurrentWeather cw=(CurrentWeather)weather;
				current_temperature.setText(stringbuilder.append(cw.getCurrent_tem()).append("\260").append("C"));
				stringbuilder.delete(0, stringbuilder.length());
				clothing(Integer.valueOf(cw.getCurrent_tem()));
			
				//Log.d("test", (new StringBuilder()).append("hint").append(hint_info.getText()).toString());
				sunsproof(Integer.valueOf(cw.getCurrent_tem()));
				current_temperature.setTextSize(65F);
				
				update_date.setText((new StringBuilder()).append("\u5DF2\u66F4\u65B0 ").append(cw.getUpdateDate()));
				humidity.setText("\u6E7F\u5EA6\uFF1A"+weather.getHumidity());
				wind.setText("\u98CE\u5411\uFF1A"+weather.getWind_condition());
			}else{
			
		
		
			current_temperature.setText(stringbuilder.append(weather.getTemperature_min()).append("\260").append("C").append(" / ")
					.append(weather.getTemperature_max()).append("\260").append("C"));
			stringbuilder.delete(0, stringbuilder.length());
			clothing(Integer.valueOf(weather.getTemperature_min()));
		
			sunsproof(Integer.valueOf(weather.getTemperature_min()));
			current_temperature.setTextSize(30F);
			humidity.setText(weather.getHumidity());
			wind.setText(weather.getWind_condition());
			}
		
			hint_info.setText(weather.getCondition());
			action();
			imageview.setImageResource(ResourceAdapter.imgs[position]);
		
			if(init){
				for(int i=0;i<forecastsDay.length;i++){
					forecastsImages[i].setImageResource(ResourceAdapter.weather_icon[i]);
					if(i!=0) //today week need not change
					forecastsDay[i].setText(weathers.get(i).getCurrently_week());
					forecastsTemprature[i].setText((new StringBuilder()).append(weathers.get(1).getTemperature_min()).append("\260").append("C")
							.append(" / ").append(weathers.get(1).getTemperature_max()).append("\260").append("C").toString());
				}
				click();
			}
			
		}
		
	}
	
//	public void initView(int i) {
//		week_info.setText(adapter.getWeatherByIndex(i).getCurrently_week());
//		// String as[] =
//		// adapter.getWeatherByIndex(i).getCurrently_date().split("-");
//		date_info.setText(adapter.getWeatherByIndex(i).getCurrently_date()); // (new
//																				// StringBuilder()).append(as[1]).append("\u6708").append(as[2]).append("\u65E5").toString());
//		StringBuilder stringbuilder = new StringBuilder();
//		stringbuilder.append("\u6E29\u5EA6\uFF1A").append(adapter.getWeatherByIndex(i).getTemperature_min()).append("\260").append("C").append(" / ")
//				.append(adapter.getWeatherByIndex(i).getTemperature_max()).append("\260").append("C");
//		temperature_view.setText(stringbuilder);
//		stringbuilder.delete(0, stringbuilder.length());
//		//Log.d("test", (new StringBuilder()).append(ResourceAdapter.imgs[i]).append("").toString());
//		action();
//		nextone_icon.setImageResource(ResourceAdapter.weather_icon[0]);
//		nexttwo_icon.setImageResource(ResourceAdapter.weather_icon[1]);
//		// nextthird_icon.setImageResource(ResourceAdapter.weather_icon[3]);
//	}

//	public void initView1() {
//		humidity.setText(adapter.getCurrentWeatherByIndex(0).getHumidity());
//		// String as[] =
//		// adapter.getCurrentWeatherByIndex(0).getWind_condition().split("\u3001");
//		wind.setText(adapter.getCurrentWeatherByIndex(0).getWind_condition());
//		imageview.setImageResource(ResourceAdapter.current_img);
//		hint_info.setText(adapter.getCurrentWeatherByIndex(0).getCondition());
//		//Log.d("test", (new StringBuilder()).append(" currentcondition=").append(adapter.getCurrentWeatherByIndex(0).getCondition()).toString());
//		update_date.setText((new StringBuilder()).append("\u5DF2\u66F4\u65B0 ").append(adapter.getWeatherByIndex(0).getCurrently_date()).toString());
//		current_temperature.setText((new StringBuilder()).append(adapter.getCurrentWeatherByIndex(0).getCurrent_tem()).append("\260").toString());
//		clothing(Integer.valueOf(adapter.getCurrentWeatherByIndex(0).getCurrent_tem()).intValue());
//		//Log.d("test", (new StringBuilder()).append("hint").append(hint_info.getText()).toString());
//		sunsproof(Integer.valueOf(adapter.getCurrentWeatherByIndex(0).getCurrent_tem()).intValue());
//		current_temperature.setTextSize(65F);
//		nextone_week.setText(adapter.getWeatherByIndex(1).getCurrently_week());
//		nexttwo_week.setText(adapter.getWeatherByIndex(2).getCurrently_week());
//		// nextthree_week.setText(adapter.getWeatherByIndex(3).getCurrently_week());
//		today_temperature.setText((new StringBuilder()).append(adapter.getWeatherByIndex(0).getTemperature_min()).append("\260").append("C")
//				.append(" / ").append(adapter.getWeatherByIndex(0).getTemperature_max()).append("\260").append("C").toString());
//		nextone_temperate.setText((new StringBuilder()).append(adapter.getWeatherByIndex(1).getTemperature_min()).append("\260").append("C")
//				.append(" / ").append(adapter.getWeatherByIndex(1).getTemperature_max()).append("\260").append("C").toString());
//		nexttwo_temperature.setText((new StringBuilder()).append(adapter.getWeatherByIndex(2).getTemperature_min()).append("\260").append("C")
//				.append(" / ").append(adapter.getWeatherByIndex(2).getTemperature_max()).append("\260").append("C").toString());
//		// nextthree_temperature.setText((new
//		// StringBuilder()).append(adapter.getWeatherByIndex(3).getTemperature_min()).append("\260").append("C")
//		// .append(" / ").append(adapter.getWeatherByIndex(3).getTemperature_max()).append("\260").append("C").toString());
//		today_icon.setImageResource(ResourceAdapter.current_icon);
//	}

//	public void initView2() {
//		humidity.setText("\u6E7F\u5EA6\uFF1A\u65E0\u6570\u636E");
//		wind.setText("\u98CE\u5411\uFF1A\u65E0\u6570\u636E");
//	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030035);
		cgWeatherAsy = new CGWeatherAsy();
		cgWeatherAsy.execute(new String[0]);
		adapter = new ResourceAdapter(this);
		week_info = (TextView) findViewById(0x7f0a0134);
		date_info = (TextView) findViewById(0x7f0a0133);
		hint_info = (TextView) findViewById(0x7f0a0135);
		temperature_view = (TextView) findViewById(0x7f0a0138);
		humidity = (TextView) findViewById(0x7f0a013b);
		wind = (TextView) findViewById(0x7f0a0139);
		current_temperature = (TextView) findViewById(0x7f0a0131);
		clothing = (TextView) findViewById(0x7f0a013e);
		action = (TextView) findViewById(0x7f0a0140);
		returnButton = (ImageView) findViewById(0x7f0a0000);
	
		forecastsImages=new ImageView[]{(ImageView) findViewById(R.id.today_weahtericon),
				(ImageView) findViewById(R.id.nextone_weahtericon),
				(ImageView) findViewById(R.id.nexttwo_weahtericon),
				(ImageView) findViewById(R.id.nextthird_weahtericon),
				(ImageView) findViewById(R.id.nextfouth_weahtericon)};
		forecastsDay=new TextView[]{(TextView) findViewById(R.id.today_weekday),
				(TextView) findViewById(R.id.nextone_weekday),
				(TextView) findViewById(R.id.nexttwo_weekday),
				(TextView) findViewById(R.id.nextthird_weekday),
				(TextView) findViewById(R.id.nextfouth_weekday)};
		forecastsTemprature=new TextView[]{(TextView) findViewById(R.id.today_temperture),
				(TextView) findViewById(R.id.nextone_temperture),
				(TextView) findViewById(R.id.nexttwo_temperture),
				(TextView) findViewById(R.id.nextthird_temperture),
				(TextView) findViewById(R.id.nextfouth_temperture)};
		forecastsLayout=new LinearLayout[]{(LinearLayout) findViewById(R.id.today_weather),
				(LinearLayout) findViewById(R.id.nextone_weather),
				(LinearLayout) findViewById(R.id.nexttwo_weather),
				(LinearLayout) findViewById(R.id.nextthird_weather),
				(LinearLayout) findViewById(R.id.nextfouth_weather)};

		imageview = (ImageView) findViewById(0x7f0a0130);
	    update_date = (TextView) findViewById(0x7f0a0136);
		sunproof = (TextView) findViewById(0x7f0a013c);
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(CG_DATA.CG_NAME);
	}

	protected void onResume() {
		super.onResume();
		if (!TCUtil.isNetAvailable(this)) {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
			builder.setTitle(getString(0x7f08007f));
			builder.setMessage(getString(0x7f080080));
			builder.setNegativeButton(getString(0x7f080082), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
				}

			});
			builder.setPositiveButton(getString(0x7f080081), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					TCUtil.startWirelessSetting(CGWeatherActivity.this);
				}

			});
			AlertDialog alertdialog = builder.create();
			alertdialog.show();
			alertdialog.setCancelable(false);
		}
		//Log.d("test", (new StringBuilder()).append("adapter.weather=").append(adapter.getWeatherByIndex(0)).toString());
		if (adapter.getWeatherByIndex(0) == null) {
			defaultView();
			returnButton.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view) {
					finish();
				}

			});
			

		} else {
			initView(0,false);
		}
	}

	public void sunsproof(int i) {
		if (hint_info.getText().equals("\u6674") || hint_info.getText().equals("\u4EE5\u6674\u4E3A\u4E3B")) {
			if (i > 30 && i != 0x1869f)
				sunproof.setText("\u9632\u6652\u6307\u6570\uFF1A SPF 50");
			else if (i > 25 && i != 0x1869f)
				sunproof.setText("\u9632\u6652\u6307\u6570\uFF1A SPF 30");
			else if (i > 20 && i != 9999)
				sunproof.setText("\u9632\u6652\u6307\u6570\uFF1A SPF 15");
			else
				sunproof.setText("\u9632\u6652\u6307\u6570\uFF1A\u4E0D\u9700\u8981");
		} else {
			sunproof.setText("\u9632\u6652\u6307\u6570\uFF1A\u4E0D\u9700\u8981");
		}
	}

	private TextView action;
	private ResourceAdapter adapter;
	private CGWeatherAsy cgWeatherAsy;
	private TextView clothing;
	private TextView current_temperature;
	private TextView date_info;
	private TextView hint_info;
	private TextView humidity;
	private ImageView imageview;
	//private ImageView nextone_icon;
	//private TextView nextone_temperate;
	//private LinearLayout nextone_weather;
	//private TextView nextone_week;
	//private ImageView nextthird_icon;
	//private TextView nextthree_temperature;
	//private LinearLayout nextthree_weather;
	//private TextView nextthree_week;
	//private ImageView nexttwo_icon;
	//private TextView nexttwo_temperature;
	//private LinearLayout nexttwo_weather;
	//private TextView nexttwo_week;
	private ImageView returnButton;
	private TextView sunproof;
	private TextView temperature_view;
	private TextView titleText;
	//private ImageView today_icon;
	//private TextView today_temperature;
	//private LinearLayout today_weahter;
	private TextView update_date;
	private TextView week_info;
	private TextView wind;
	private ImageView[] forecastsImages;
	private TextView[] forecastsDay;
	private TextView[] forecastsTemprature;
	private LinearLayout[] forecastsLayout;

}
