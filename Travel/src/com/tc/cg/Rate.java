//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.tc.TCUtil;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.Date;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class Rate extends CGBaseActivity
    implements android.view.View.OnTouchListener, android.view.View.OnClickListener, android.view.View.OnFocusChangeListener
{

    public Rate()
    {
        handler = new Handler() {

            public void handleMessage(Message message)
            {
                switch(message.what){
                case -3:
                	currency_state.setText((new StringBuilder()).append(getString(0x7f0800cf)).append(rate_time).toString());
                    Toast.makeText(Rate.this, 0x7f0800d2, 0).show();
                    break;
                    
                case -2:
                	   currency_state.setText((new StringBuilder()).append(getString(0x7f0800d1)).append(getString(0x7f0800d3)).append(getString(0x7f0800cf)).append(rate_time).toString());
                       Toast.makeText(Rate.this, 0x7f0800d1, 0).show();
                       break;
                case -1:
                	 currency_state.setText((new StringBuilder()).append(getString(0x7f0800ce)).append(getString(0x7f0800d3)).append(getString(0x7f0800cf)).append(rate_time).toString());
                     Toast.makeText(Rate.this, 0x7f0800ce, 0).show();
                     break;
                }
          
            }

           
        }
;
    }

    private int getIconCode(int i)
    {
        int j = 0;
        switch(i){
          case 0x7f0a0203:
            	j= 7;
            	break;
          case 0x7f0a0204:
          	j= 8;
          	break;
          case 0x7f0a0205:
          	j= 9;
          	break;
          case 0x7f0a0206:
            	
            	break;
          case 0x7f0a0207:
        	  j=4;
        	  break;
          case 0x7f0a0208:
        	  j=5;
        	  break;  
          case 0x7f0a0209:
        	  j=6;
        	  break;   
          case 	0x7f0a020a:
        	  break;
          case 	0x7f0a020b:
        	  j=1;
        	  break;
          case 	0x7f0a020c:
        	  j=2;
        	  break;  
          case 	0x7f0a020d:
        	  j=3;
        	  break;
          case 	0x7f0a020e:
        	  
        	  break;  
        }
        return j;
    
    }

    private void resetCursor()
    {
        EditText edittext = whichEditText(whichEditText);
        edittext.setSelection(edittext.getText().length());
    }

    private void setCurrencyMoney(EditText edittext, EditText edittext1)
    {
    	 try
         {
        if(edittext.getId() == 0x7f0a01fd){
        
        	 money_2ET.setText(tooLong(Double.parseDouble(money_1ET.getText().toString()) * (double)rate));
        	 
      
        }else{
        	  
                   money_1ET.setText(tooLong(Double.parseDouble(money_2ET.getText().toString()) / (double)rate));
             
        }
         }
         catch(NumberFormatException e)
         {
             money_1ET.setText("0.00");
         }
    
    }

    private String tooLong(double d)
    {
        String s = moneyFormatter.format(d);
        if(s.length() > 12)
            s = "0";
        return s;
    }

    private void updateCurrency()
    {
        Date date1;
        try
        {
            date = dateFormatter.parse(rate_time);
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
        }
        date1 = new Date(System.currentTimeMillis());
        if((date1.getTime() - date.getTime()) / 0x36ee80L <= 12L)
        	 handler.sendEmptyMessage(-3);
        else{
        	try{
        rate = Float.parseFloat((new BufferedReader(new InputStreamReader(((HttpURLConnection)(new URL((new StringBuilder()).append("http://download.finance.yahoo.com/d/quotes.csv?e=.csv&f=l1&s=").append(CG_DATA.CG_RATE_CODE).append("=X").toString())).openConnection()).getInputStream()))).readLine());
        date = date1;
        rate_time = dateFormatter.format(date);
        handler.sendEmptyMessage(-2);
        	}catch(MalformedURLException e){
        		Log.e("Rate","",e);
        	}catch(IOException e){
        		 handler.sendEmptyMessage(-1);
        		Log.e("Rate","",e);	
        	}
        }

    }

    private EditText whichEditText(int i)
    {
        switch(i){
         case 0x7f0a01fd:
        	 return money_1ET;
        	
         case 0x7f0a01fe:
         case 0x7f0a01ff:
        	 return money_1ET;
         case 0x7f0a0200:
        	 return money_2ET;
        	 default :return money_1ET;
        }
    
    }

    public void onClick(View view)
    {
        int i = view.getId();
        switch(getCurrentFocus().getId()){
        case 0x7f0a01fd:
        	 whichEditText = 0x7f0a01fd;
       	     break;
        case 0x7f0a01fe:
        case 0x7f0a01ff:
        	break;
        case 0x7f0a0200:
        	whichEditText = 0x7f0a0200;
        	break;
        }
        String s = whichEditText(whichEditText).getText().toString();
        if(s.equals("0"))
            s = "";
        
        
        switch(i)
        {
        default:
            if(i == 0x7f0a01fd || i == 0x7f0a0200)
            {
                keyboard.setVisibility(0);
                resetCursor();
            } else
            {
                if(s.equals("0.00"))
                    s = "";
                whichEditText(whichEditText).setText((new StringBuilder()).append(s).append(getIconCode(view.getId())).toString());
                resetCursor();
            }
            break;

        case 2131362310: 
        	 whichEditText(whichEditText).setText("0");
             resetCursor();

        case 2131362314: 
        	 if(s.equals("") || s.length() == 1)
                  whichEditText(whichEditText).setText("0");
              else
                  whichEditText(whichEditText).setText(s.subSequence(0, -1 + s.length()));
              if(whichEditText == 0x7f0a01fd)
                  setCurrencyMoney(money_1ET, money_2ET);
              if(whichEditText == 0x7f0a0200)
                  setCurrencyMoney(money_2ET, money_1ET);
              resetCursor();
              break;
        case 2131362319: 
        	  if(s.equals(""))
                  s = "0";
              if(s.matches("^[0-9]*$"))
                  whichEditText(whichEditText).setText((new StringBuilder()).append(s).append(".").toString());
              resetCursor();
              break;
        }
        if(i == 0x7f0a01fd)
            money_1ET.setSelection(money_1ET.length());
        else
        if(i == 0x7f0a0200)
            money_2ET.setSelection(money_2ET.length());
    }
        
   
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030059);
        getWindow().addFlags(0x20000);
        imageViewBack = (ImageView)findViewById(0x7f0a0153);
        imageViewBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                onBackPressed();
            }

         
        }
);
        rate = CG_DATA.CG_RATE;
        rate_time = CG_DATA.CG_RATE_TIME;
        dateFormatter = new SimpleDateFormat(getString(0x7f0800d4));
        SharedPreferences sharedpreferences = getSharedPreferences("Tip_Rate", 0);
        rate = sharedpreferences.getFloat("rate", rate);
        rate_time = sharedpreferences.getString("rate_time", rate_time);
        moneyFormatter = new DecimalFormat("0.00");
        whichEditText = 0x7f0a01fd;
        keyboard = (TableLayout)findViewById(0x7f0a0202);
        money_1ET = (EditText)findViewById(0x7f0a01fd);
        money_1ET.setOnFocusChangeListener(this);
        money_1ET.setOnClickListener(this);
        money_1ET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable)
            {
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
                if(whichEditText == 0x7f0a01fd)
                    setCurrencyMoney(money_1ET, money_2ET);
            }

        }
);
        resetCursor();
        money_2ET = (EditText)findViewById(0x7f0a0200);
        money_2ET.setOnFocusChangeListener(this);
        money_2ET.setOnClickListener(this);
        money_2ET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable)
            {
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
                if(whichEditText == 0x7f0a0200)
                    setCurrencyMoney(money_2ET, money_1ET);
            }

         
        }
);
        country_1TV = (TextView)findViewById(0x7f0a01fc);
        country_1TV.setText(CG_DATA.CG_RATE_FROM);
        country_1TV.setOnClickListener(this);
        country_2TV = (TextView)findViewById(0x7f0a01ff);
        country_2TV.setText(CG_DATA.CG_RATE_TO);
        country_2TV.setOnClickListener(this);
        currency_state = (TextView)findViewById(0x7f0a0201);
        currency_state.setText((new StringBuilder()).append(getString(0x7f0800cf)).append(rate_time).toString());
        keyboard = (TableLayout)findViewById(0x7f0a0202);
        x0 = (ImageButton)findViewById(0x7f0a020e);
        x0.setOnTouchListener(this);
        x0.setOnClickListener(this);
        x1 = (ImageButton)findViewById(0x7f0a020b);
        x1.setOnTouchListener(this);
        x1.setOnClickListener(this);
        x2 = (ImageButton)findViewById(0x7f0a020c);
        x2.setOnTouchListener(this);
        x2.setOnClickListener(this);
        x3 = (ImageButton)findViewById(0x7f0a020d);
        x3.setOnTouchListener(this);
        x3.setOnClickListener(this);
        x4 = (ImageButton)findViewById(0x7f0a0207);
        x4.setOnTouchListener(this);
        x4.setOnClickListener(this);
        x5 = (ImageButton)findViewById(0x7f0a0208);
        x5.setOnTouchListener(this);
        x5.setOnClickListener(this);
        x6 = (ImageButton)findViewById(0x7f0a0209);
        x6.setOnTouchListener(this);
        x6.setOnClickListener(this);
        x7 = (ImageButton)findViewById(0x7f0a0203);
        x7.setOnTouchListener(this);
        x7.setOnClickListener(this);
        x8 = (ImageButton)findViewById(0x7f0a0204);
        x8.setOnTouchListener(this);
        x8.setOnClickListener(this);
        x9 = (ImageButton)findViewById(0x7f0a0205);
        x9.setOnTouchListener(this);
        x9.setOnClickListener(this);
        dot = (ImageButton)findViewById(0x7f0a020f);
        dot.setOnTouchListener(this);
        dot.setOnClickListener(this);
        cancel = (ImageButton)findViewById(0x7f0a0206);
        cancel.setOnTouchListener(this);
        cancel.setOnClickListener(this);
        del = (ImageButton)findViewById(0x7f0a020a);
        del.setOnTouchListener(this);
        del.setOnClickListener(this);
        (new Thread(new Runnable() {

            public void run()
            {
                updateCurrency();
            }

        }
)).start();
    }

    protected void onDestroy()
    {
        android.content.SharedPreferences.Editor editor = getSharedPreferences("Tip_Rate", 0).edit();
        editor.putFloat("rate", rate);
        editor.putString("rate_time", rate_time);
        editor.commit();
        TCUtil.hideSoftKeyBroad(this, money_1ET);
        super.onDestroy();
    }

    public void onFocusChange(View view, boolean flag)
    {
        if(flag){   //goto _L2; else goto _L1
        	 if(view.getId() == 0x7f0a01fd){
        		  money_1ET.setSelection(money_1ET.length());
        	 }else{
        		 if(view.getId() == 0x7f0a0200)
        	            money_2ET.setSelection(money_2ET.length());
        	 }
        	 keyboard.setVisibility(0);
        }

    }

    protected void onResume()
    {
        super.onResume();
        money_1ET.setSelection(money_1ET.length());
    }

    public boolean onTouch(View view, MotionEvent motionevent)
    {
        if(motionevent.getAction() == 0){ //goto _L2; else goto _L1
        	view.getBackground().setAlpha(70);
            view.setBackgroundDrawable(view.getBackground());
        }else if(motionevent.getAction() == 1)
        {
            view.getBackground().setAlpha(255);
            view.setBackgroundDrawable(view.getBackground());
        }
        return false;
    }

    private static final int CURRENCY_AVALIABLE = -3;
    private static final int UPDATE_CURRENCY_FAIL = -1;
    private static final int UPDATE_CURRENCY_SECCESS = -2;
    private static final int UPDATE_CURRENCY_TIME = 12;
    ImageButton cancel;
    TextView country_1TV;
    TextView country_2TV;
    TextView currency_state;
    Date date;
    SimpleDateFormat dateFormatter;
    ImageButton del;
    ImageButton dot;
    Handler handler;
    private ImageView imageViewBack;
    TableLayout keyboard;
    DecimalFormat moneyFormatter;
    EditText money_1ET;
    EditText money_2ET;
    float rate;
    String rate_time;
    int whichEditText;
    ImageButton x0;
    ImageButton x1;
    ImageButton x2;
    ImageButton x3;
    ImageButton x4;
    ImageButton x5;
    ImageButton x6;
    ImageButton x7;
    ImageButton x8;
    ImageButton x9;


}
