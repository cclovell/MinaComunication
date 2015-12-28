/* 
 * @(#)UUidUtils.java    Created on 2015-12-22
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.util;

import java.util.UUID;

public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
