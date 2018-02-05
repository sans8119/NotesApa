// **************************************************************************
// Copyright 2016 Honeywell International Sarl
// **************************************************************************
package com.pp.asn;

import android.app.Application;

import com.pp.asn.db.DbUtils;
import com.pp.asn.utils.Utils;

public class PhoneApplication extends Application {
    private Utils utils;
    private DbUtils dbUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new Utils();
        dbUtils = new DbUtils();
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    public Utils getUtils() {
        return utils;
    }
}
