package com.supermap.gaf.services.resource;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.LocaleData;

@BaseName("CommonResource")
@LocaleData(defaultCharset = "UTF-8", value = {@ch.qos.cal10n.Locale("zh_CN")})
public enum CommonResource {
    DESUTIL_ENCRYPTION_FAILED,
    DESUTIL_DECRYPTION_FAILED
}
