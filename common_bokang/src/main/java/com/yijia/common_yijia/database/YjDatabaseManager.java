package com.yijia.common_yijia.database;

import android.content.Context;



import org.greenrobot.greendao.database.Database;

public class YjDatabaseManager {

    private DaoSession mDaoSession = null;
    private YjUserProfileDao mDao = null;

    private YjDatabaseManager(){

    }

    public YjDatabaseManager init(Context context){
        initDao(context);
        return this;

    }

    private static final class Holder{
        private static final YjDatabaseManager INSTANCE=new YjDatabaseManager();
    }

    public static YjDatabaseManager getInstance(){
        return Holder.INSTANCE;
    }

    private void initDao(Context context) {
        final YjReleaseOpenHelper helper = new YjReleaseOpenHelper(context, "yijia.db");
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getYjUserProfileDao();
    }

    public final YjUserProfileDao getDao(){
        return mDao;
    }
}
