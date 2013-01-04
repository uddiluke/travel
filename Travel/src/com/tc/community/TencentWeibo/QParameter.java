// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community.TencentWeibo;

import java.io.Serializable;

public class QParameter
    implements Serializable, Comparable
{

    public QParameter(String s, String s1)
    {
        name = s;
        value = s1;
    }

    public int compareTo(QParameter qparameter)
    {
        int i = name.compareTo(qparameter.getName());
        if(i == 0)
            i = value.compareTo(qparameter.getValue());
        return i;
    }

    public  int compareTo(Object obj)
    {
        return compareTo((QParameter)obj);
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setValue(String s)
    {
        value = s;
    }

    private static final long serialVersionUID = 0x8285588f3b7c20d3L;
    private String name;
    private String value;
}
