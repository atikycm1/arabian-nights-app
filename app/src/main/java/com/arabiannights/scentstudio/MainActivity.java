package com.arabiannights.scentstudio;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.widget.*;
import android.graphics.drawable.GradientDrawable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class MainActivity extends Activity {
  private final int gold=0xffd9b570, bg=0xff0d0d0f;
  private LinearLayout products;
  private String query="";
  private final ArrayList<JSONObject> added=new ArrayList<>();
  private final String[][] catalog={
    {"Ultra Male","90"},{"Ombre Nomade","100"},{"212 VIP","100"},{"Imperial Oud","90"},
    {"Royal Oud Elixir","90"},{"Musk Vanilla","80"},{"Hawas Black","80"},
    {"Musk Taharah Rose","90"},{"Good Girl","80"},{"Bombshell","90"},
    {"Paris Hilton","80"},{"100 Silent Ways","100"},{"Stronger With You Intensely","90"},
    {"PDM Al Tahir","100"},{"YSL Y","100"},{"White Oud","120"},
    {"Bin Sheikh","90"},{"Arabian Bakhoor","80"},{"Misk Fakher Supreme","150"},
    {"Baccarat Rouge","90"}
  };
  @Override public void onCreate(Bundle b){
    super.onCreate(b);load();
    ScrollView scroll=new ScrollView(this);
    LinearLayout root=new LinearLayout(this);root.setOrientation(1);
    root.setPadding(24,28,24,24);root.setBackgroundColor(bg);scroll.addView(root);
    TextView logo=label("AN",42,gold,true);logo.setGravity(17);root.addView(logo);
    TextView brand=label("ARABIAN NIGHTS",24,gold,true);brand.setGravity(17);root.addView(brand);
    TextView tag=label("1ML · TEST · TRUST",12,0xffcccccc,false);tag.setGravity(17);root.addView(tag);
    TextView title=label("My Collection",23,Color.WHITE,true);title.setPadding(0,32,0,8);root.addView(title);
    Button add=new Button(this);add.setText("+ Add New Attar");
    add.setOnClickListener(v->edit(null));root.addView(add);
    EditText search=new EditText(this);search.setSingleLine(true);
    search.setHint("Search fragrances");search.setHintTextColor(0xffaaaaaa);search.setTextColor(Color.WHITE);
    root.addView(search);
    search.addTextChangedListener(new TextWatcher(){
      public void beforeTextChanged(CharSequence s,int start,int count,int after){}
      public void onTextChanged(CharSequence s,int start,int before,int count){query=s.toString();render();}
      public void afterTextChanged(Editable e){}
    });
    TextView subtitle=label("Prices in BDT · 1 / 3 / 6 / 12 ml",12,0xffbbbbbb,false);
    subtitle.setPadding(0,14,0,12);root.addView(subtitle);
    products=new LinearLayout(this);products.setOrientation(1);root.addView(products);
    setContentView(scroll);render();
  }
  private TextView label(String value,int size,int color,boolean bold){
    TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);
    if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;
  }
  private int rounded(double n){return (int)(Math.floor(n/10.0+0.5)*10);}
  private void load(){
    try {JSONArray a=new JSONArray(getPreferences(0).getString("collection","[]"));
      for(int i=0;i<a.length();i++)added.add(a.getJSONObject(i));
    }catch(Exception ignored){}
  }
  private void save(){
    JSONArray a=new JSONArray();for(JSONObject o:added)a.put(o);
    getPreferences(0).edit().putString("collection",a.toString()).apply();
  }
  private EditText field(LinearLayout parent,String hint,String value,boolean number){
    EditText e=new EditText(this);e.setHint(hint);e.setText(value);e.setSingleLine(true);
    if(number)e.setInputType(InputType.TYPE_CLASS_NUMBER);
    parent.addView(e);return e;
  }
  private void edit(JSONObject existing){
    LinearLayout form=new LinearLayout(this);form.setOrientation(1);form.setPadding(28,8,28,8);
    ScrollView sc=new ScrollView(this);sc.addView(form);
    EditText name=field(form,"Attar name",existing==null?"":existing.optString("name"),false);
    EditText price=field(form,"1 ml price (BDT)",existing==null?"":existing.optString("price"),true);
    EditText notes=field(form,"Fragrance notes",existing==null?"":existing.optString("notes"),false);
    EditText season=field(form,"Season / occasion",existing==null?"":existing.optString("season"),false);
    CheckBox stock=new CheckBox(this);stock.setText("In stock");stock.setChecked(existing==null||existing.optBoolean("stock",true));form.addView(stock);
    AlertDialog dialog=new AlertDialog.Builder(this)
      .setTitle(existing==null?"Add New Attar":"Edit Attar").setView(sc)
      .setNegativeButton("Cancel",null).setPositiveButton("Save",null).create();
    dialog.setOnShowListener(d->dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
      String n=name.getText().toString().trim(),p=price.getText().toString().trim();
      int value;
      try{value=Integer.parseInt(p);}catch(Exception e){value=0;}
      if(n.isEmpty()||value<=0){Toast.makeText(this,"Enter a name and valid 1 ml price",Toast.LENGTH_LONG).show();return;}
      JSONObject obj=existing==null?new JSONObject():existing;
      try{
        obj.put("name",n);obj.put("price",value);obj.put("notes",notes.getText().toString().trim());
        obj.put("season",season.getText().toString().trim());obj.put("stock",stock.isChecked());
      }catch(Exception ignored){}
      if(existing==null)added.add(obj);
      save();render();dialog.dismiss();
    }));
    dialog.show();
  }
  private void render(){
    if(products==null)return;
    products.removeAllViews();int count=0;
    for(String[] item:catalog){
      if(!item[0].toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))continue;
      product(item[0],Integer.parseInt(item[1]),null);count++;
    }
    for(JSONObject item:added){
      String name=item.optString("name");
      if(!name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))continue;
      product(name,item.optInt("price"),item);count++;
    }
    if(count==0)products.addView(label("No matching fragrances.",15,Color.WHITE,false));
  }
  private void product(String name,int price,JSONObject editable){
    LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setPadding(18,14,18,14);
    GradientDrawable shape=new GradientDrawable();shape.setColor(0xff222225);
    shape.setCornerRadius(18);shape.setStroke(1,0xff705a3d);card.setBackground(shape);
    card.addView(label(name,18,Color.WHITE,true));
    card.addView(label("1ml ৳"+price+"  ·  3ml ৳"+rounded(price*2.75)+"  ·  6ml ৳"+rounded(price*5)+"  ·  12ml ৳"+rounded(price*9.375),13,gold,false));
    if(editable!=null){
      String notes=editable.optString("notes"),season=editable.optString("season");
      if(!notes.isEmpty())card.addView(label(notes,13,0xffcccccc,false));
      if(!season.isEmpty())card.addView(label(season,13,0xffcccccc,false));
      card.addView(label(editable.optBoolean("stock",true)?"In stock":"Out of stock",12,gold,false));
      LinearLayout actions=new LinearLayout(this);
      Button edit=new Button(this);edit.setText("Edit");edit.setOnClickListener(v->edit(editable));actions.addView(edit);
      Button delete=new Button(this);delete.setText("Delete");
      delete.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Delete "+name+"?")
        .setMessage("This removes the attar from this device.")
        .setNegativeButton("Cancel",null)
        .setPositiveButton("Delete",(d,w)->{added.remove(editable);save();render();}).show());
      actions.addView(delete);card.addView(actions);
    }
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,12);
    products.addView(card,lp);
  }
}
