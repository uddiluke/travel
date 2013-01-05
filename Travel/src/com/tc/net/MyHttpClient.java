//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.net;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

// Referenced classes of package com.tc.net:
//            FakeSocketFactory

public class MyHttpClient extends DefaultHttpClient
{

    public MyHttpClient(HttpParams httpparams)
    {
        super(httpparams);
    }

    protected ClientConnectionManager createClientConnectionManager()
    {
        SchemeRegistry schemeregistry = new SchemeRegistry();
        schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeregistry.register(new Scheme("https", new FakeSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), schemeregistry);
    }
}
