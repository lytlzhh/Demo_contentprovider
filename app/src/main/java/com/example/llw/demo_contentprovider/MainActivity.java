package com.example.llw.demo_contentprovider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //用于客户端访问contentprovider
        ContentResolver contentResolver = getContentResolver();

        Cursor c = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex("_id"));
                Log.i(TAG, "onCreate: " + id);

                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i(TAG, "onCreate: " + name);

                Cursor c1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

                //根据联系人的id查询出联系人的号码
                if (c1 != null) {
                    while (c1.moveToNext()) {
                        int phone_type = c1.getInt(c1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                        if (phone_type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                            Log.i(TAG, "onCreate:家庭号码： " + c1.getString(c1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                        } else if (phone_type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            Log.i(TAG, "onCreate:移动号码： " + c1.getString(c1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }

                    }
                    c1.close();
                }

                //根据联系人的id查询出联系人的email
                Cursor c2 = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Email.DATA,
                        ContactsContract.CommonDataKinds.Email.TYPE}, ContactsContract.CommonDataKinds.Email._ID + "=" + id, null, null);

                if (c2 != null) {
                    while (c2.moveToNext()) {
                        int email_type = c2.getInt(c2.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        if (email_type == ContactsContract.CommonDataKinds.Email.TYPE_WORK) {
                            Log.i(TAG, "onCreate:邮箱类型是： " + c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                        }
                    }
                    c2.close();
                }
            }

            c.close();
        }


    }


}
