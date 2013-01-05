//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import java.io.Serializable;

public class CommunityData
    implements Serializable
{

    public CommunityData(int i, String s, int j, String s1, String s2, int k, int l, 
            int i1, String s3, String s4, int j1)
    {
        id = i;
        pointType = s;
        pointId = j;
        content = s1;
        userName = s2;
        like = k;
        reply = l;
        star = i1;
        if("null".equals(s3) || "".equals(s3))
            img = null;
        else
            img = s3;
        createTime = s4;
        fresh = j1;
        source = null;
    }

    public CommunityData(int i, String s, int j, String s1, String s2, int k, int l, 
            int i1, String s3, String s4, String s5)
    {
        id = i;
        pointType = s;
        pointId = j;
        content = s1;
        userName = s2;
        like = k;
        reply = l;
        star = i1;
        if("null".equals(s3) || "".equals(s3))
            img = null;
        else
            img = s3;
        createTime = s4;
        fresh = 0;
        if("null".equals(s5) || "".equals(s5))
            source = null;
        else
            source = s5;
    }

    public String content;
    public String createTime;
    public int fresh;
    public int id;
    public String img;
    public int like;
    public int pointId;
    public String pointType;
    public int reply;
    public String source;
    public int star;
    public String userName;
}
