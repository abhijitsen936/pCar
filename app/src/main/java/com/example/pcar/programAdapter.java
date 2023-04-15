package com.example.pcar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pcar.sqlite;





public class programAdapter extends ArrayAdapter<String> {
    Context context;
    int[] id;
    byte[] b;
    String[] carMake;
    String[] carModel;

    Bitmap bitmap;




    public programAdapter( Context context, String[] carMake, String[] carModel, int[] id, byte[] b) {

        super(context, R.layout.list_item, R.id.nameTextView, carMake);
        this.context=context;
        this.id=id;
        this.carMake=carMake;
        this.carModel=carModel;
        this.b=b;
    }
    public static class ProgramViewHolder {
        ImageView carPics;
        TextView carMake;
        TextView carModel;
        Button setImage, delete;


        ProgramViewHolder(View v){
            carMake=v.findViewById(R.id.nameTextView);
            carModel=v.findViewById(R.id.idTextView);
            carPics=v.findViewById(R.id.imageView);
            setImage=v.findViewById(R.id.setImageButton);
            delete=v.findViewById(R.id.deleteButton);
        }


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProgramViewHolder holder;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder= new ProgramViewHolder(convertView);

            holder.carPics= convertView.findViewById(R.id.imageView);
            holder.carMake= convertView.findViewById(R.id.nameTextView);
            holder.carModel= convertView.findViewById(R.id.idTextView);
            holder.delete= convertView.findViewById(R.id.deleteButton);
            holder.setImage= convertView.findViewById(R.id.setImageButton);

            convertView.setTag(holder);
        } else {
            holder=(ProgramViewHolder) convertView.getTag();
        }

        holder.carMake.setText(carMake[position]);
        holder.carModel.setText(carModel[position]);

        // Decode byte array into Bitmap and set to ImageView
        byte[] imageBytes = b;
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.carPics.setImageBitmap(bitmap);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteOpenHelper dbHelper = new sqlite(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor =db.rawQuery("select * from users where carMake=?", new String[]{carMake[position]});
                if(cursor.getCount()>0){
                    long r=db.delete("users", "carMake=?", new String[]{carMake[position]});
                    if(r==-1)  Toast.makeText(context, "Faild Deleting", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "delete "+ carMake[position], Toast.LENGTH_SHORT).show();
//                    MainActivity mc = new MainActivity();
//                    mc.makeList();
                }else
                    Toast.makeText(context, "Faild Deleting", Toast.LENGTH_SHORT).show();
            }
        });
        holder.setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image img= new image((Activity) context, id[position]);

                Bitmap b= img.saveImg(id[position]);

                holder.carPics.setImageBitmap(b);

                Toast.makeText(getContext(), "Picture saved for "+ id[position], Toast.LENGTH_SHORT).show();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "You Clicked:"+carMake[position], Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }


}