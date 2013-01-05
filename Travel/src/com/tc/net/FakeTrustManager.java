//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.net;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class FakeTrustManager
    implements X509TrustManager
{

    public FakeTrustManager()
    {
    }

    public void checkClientTrusted(X509Certificate ax509certificate[], String s)
        throws CertificateException
    {
    }

    public void checkServerTrusted(X509Certificate ax509certificate[], String s)
        throws CertificateException
    {
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return _AcceptedIssuers;
    }

    public boolean isClientTrusted(X509Certificate ax509certificate[])
    {
        return true;
    }

    public boolean isServerTrusted(X509Certificate ax509certificate[])
    {
        return true;
    }

    private static final X509Certificate _AcceptedIssuers[] = new X509Certificate[0];

}
