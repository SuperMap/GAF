package com.supermap.gaf.common.storage.annotations;

import com.supermap.gaf.common.storage.config.StorageServerAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(StorageServerAutoConfiguration.class)
public @interface EnableStorageServer{
}