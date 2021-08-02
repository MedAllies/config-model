/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
   in the documentation and/or other materials provided with the distribution.  
3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote 
   products derived from this software without specific prior written permission.
   
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.nhindirect.config.model;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import org.nhindirect.config.model.exceptions.CertificateConversionException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


/**
 * A Direct trust bundle.
 * @author Greg Meyer
 * @since 1.0
 */
///CLOVER:OFF
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrustBundle
{
	
	private long id = -1;
	private String bundleName;
	private String bundleURL;
    private byte[] signingCertificateData;
    private Collection<TrustBundleAnchor> trustBundleAnchors;
    private int refreshInterval;
    private Calendar lastRefreshAttempt;
    private BundleRefreshError lastRefreshError;
    private Calendar lastSuccessfulRefresh;    
    private Calendar createTime;  
    private String checkSum;

	/**
	 * Gets the trust anchors in the bundle.
	 * @return The trust anchors in the bundle.
	 */
	public Collection<TrustBundleAnchor> getTrustBundleAnchors() 
	{
		if (trustBundleAnchors == null)
			trustBundleAnchors = Collections.emptyList();
		
		return Collections.unmodifiableCollection(trustBundleAnchors);
	}
	
	/**
	 * Sets the trust anchors in the bundle.
	 * @param trustBundleAnchors The trust anchors in the bundle.
	 */
	public void setTrustBundleAnchors(Collection<TrustBundleAnchor> trustBundleAnchors) 
	{
		this.trustBundleAnchors = new ArrayList<TrustBundleAnchor>(trustBundleAnchors);
	}
		
	@JsonIgnore
	/**
	 * The returned value is derived from the internal byte stream representation.  This attribute is suppressed during JSON conversion.
	 */
	public X509Certificate getSigningCertificateAsX509Certificate()
	{
		
		if (signingCertificateData == null || signingCertificateData.length == 0)
			return null;
		
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(signingCertificateData))
        {
            
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
        } 
        catch (Exception e) 
        {
            throw new CertificateConversionException("Data cannot be converted to a valid X.509 Certificate", e);
        }

	}		
    
}
///CLOVER:ON
