package org.os890.ds;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.message.MessageContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Labels {
    private MessageContext labelMessageContext;

    protected Labels() {
    }

    @Inject
    public Labels(MessageContext messageContext) {
        labelMessageContext = messageContext.messageSource("META-INF/menu_labels");
    }

    public String getSectionLabel(Class<?> section) {
        return labelMessageContext.message().template("{" + section.getSimpleName().toLowerCase() + "}").toString();
    }

    public String getSubMenuLabel(Class<?> section, Class<? extends ViewConfig> subMenu) {
        return labelMessageContext.message().template("{" + section.getSimpleName().toLowerCase() + "." + subMenu.getSimpleName().toLowerCase() + "}").toString();
    }
}
