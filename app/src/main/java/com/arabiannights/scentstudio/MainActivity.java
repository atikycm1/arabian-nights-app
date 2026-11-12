package com.arabiannights.scentstudio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import android.graphics.drawable.GradientDrawable;
import java.util.*;

public class MainActivity extends Activity {
  private LinearLayout products;
  private final String[][] catalog = {
    {"Ultra Male","90"},{"Ombre Nomade","100"},{"212 VIP","100"},
    {"Imperial Oud","90"},{"Royal Oud Elixir","90"},{"Musk Vanilla","80"},
    {"Hawas Black","80"},{"Musk Taharah Rose","90"},{"Good Girl","80"},
    {"Bombshell","90"},{"Paris Hilton","80"},{"100 Silent Ways","100"},
    {"Stronger With You Intensely","90"},{"PDM Al Tahir","100"},
    {"YSL Y","100"},{"White Oud","120"},{"Bin Sheikh","90"},
    {"Arabian Bakhoor","80"},{"Misk Fakher Supreme","150"},
    {"Baccarat Rouge","90"}
  };
  private int gold=Color.rgb(217,181,112);
  private int bg=Color.rgb(13,13,15);
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    ScrollView scroll=new ScrollView(this);
    scroll.setFillViewport(true);
    LinearLayout root=new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setPadding(22,32,22,24);
    root.setBackgroundColor(bg);
    scroll.addView(root);
    TextView mark=text("AN",42,gold,true);
    mark.setGravity(17); root.addView(mark);
    TextView brand=text("ARABIAN NIGHTS",25,gold,true);
    brand.setGravity(17);root.addView(brand);
    TextView tagline=text("1ML  ·  TEST  ·  TRUST",12,0xffd7d0c4,false);
    tagline.setGravity(17);root.addView(tagline);
    TextView heading=text("Discover your signature scent",20,Color.WHITE,true);
    heading.setPadding(0,40,0,12);root.addView(heading);
    EditText search=new EditText(this);
    search.setSingleLine(true);search.setTextColor(Color.WHITE);
    search.setHintTextColor(0xff999999);search.setHint("Search perfumes...");
    root.addView(search);
    TextView note=text("Prices in BDT · 1ml / 3ml / 6ml / 12ml",12,0xffbcbcbc,false);
    note.setPadding(0,18,0,12);root.addView(note);
    products=new LinearLayout(this);products.setOrientation(LinearLayout.VERTICAL);root.addView(products);
    render("");
    search.addTextChangedListener(new TextWatcher(){
      public void beforeTextChanged(CharSequence s,int start,int count,int after){}
      public void onTextChanged(CharSequence s,int start,int before,int count){render(s.toString());}
      public void afterTextChanged(Editable e){}
    });
    setContentView(scroll);
  }
  private TextView text(String value,int size,int color,boolean bold){
    TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);
    if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    return t;
  }
  private void render(String query){
    products.removeAllViews();
    int count=0;
    for(String[] item:catalog){
      if(!item[0].toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))continue;
      count++;
      int p=Integer.parseInt(item[1]);
      LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);
      card.setPadding(18,16,18,16);
      GradientDrawable shape=new GradientDrawable();
      shape.setColor(0xff202023);shape.setCornerRadius(18);shape.setStroke(1,0xff665238);
      card.setBackground(shape);
      TextView name=text(item[0],18,Color.WHITE,true);card.addView(name);
      TextView price=text("1ml ৳"+p+"   ·   3ml ৳"+round(p*2.75)+"   ·   6ml ৳"+round(p*5.0)+"   ·   12ml ৳"+round(p*9.375),13,gold,false);
      price.setPadding(0,8,0,0);card.addView(price);
      LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,12);
      products.addView(card,lp);
    }
    if(count==0)products.addView(text("No matching fragrances.",16,Color.WHITE,false));
  }
  private int round(double n){return (int)(Math.floor(n/10.0+0.5)*10);}
}
