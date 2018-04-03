package org.os890.ds;

import org.apache.deltaspike.core.api.config.view.metadata.ViewMetaData;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ViewMetaData

@Target(TYPE)
@Retention(RUNTIME)
public @interface MenuEntry {
    int pos() default -1 /*default-value not needed with v1.8.2+*/;
}
