package com.yijia.common_yijia.database;

import android.content.Context;



import org.greenrobot.greendao.database.Database;

public class YjReleaseOpenHelper extends DaoMaster.OpenHelper{
    public YjReleaseOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }
}
