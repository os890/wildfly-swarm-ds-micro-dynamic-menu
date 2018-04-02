package org.os890.pf;

import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.jsf.api.config.view.View;
import org.os890.ds.MenuEntry;
import org.os890.ds.Pages;
import org.primefaces.model.menu.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@WindowScoped
public class MenuModelProvider implements Serializable {
    private MenuModel menuModel = new DefaultMenuModel();

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @PostConstruct
    protected void initMenu() {
        List<ConfigDescriptor<?>> allConfigDescriptors = viewConfigResolver.getConfigDescriptors();

        List<ConfigDescriptor<?>> sections = new ArrayList<>();

        for (ConfigDescriptor<?> currentConfigDescriptor : allConfigDescriptors) {
            if (currentConfigDescriptor.getConfigClass().isInterface() && currentConfigDescriptor.getConfigClass().isAnnotationPresent(MenuEntry.class)) {
                sections.add(currentConfigDescriptor);
            }
        }

        sections.sort(Comparator.comparingInt(cd -> cd.getConfigClass().getAnnotation(MenuEntry.class).pos()));

        for (ConfigDescriptor<?> configDescriptor : sections) {
            DefaultSubMenu subMenu = new DefaultSubMenu(configDescriptor.getConfigClass().getSimpleName());
            subMenu.setExpanded(true);

            addItems(allConfigDescriptors, subMenu, configDescriptor);

            menuModel.addElement(subMenu);
        }

    }

    private void addItems(List<ConfigDescriptor<?>> allConfigDescriptors, DefaultSubMenu parentMenuEntry, ConfigDescriptor<?> parentConfigDescriptor) {
        List<ViewConfigDescriptor> subMenuItems = new ArrayList<>();

        for (ConfigDescriptor<?> currentConfigDescriptor : allConfigDescriptors) {
            if (currentConfigDescriptor.getConfigClass().isInterface() || !currentConfigDescriptor.getConfigClass().isAnnotationPresent(MenuEntry.class)) {
                continue;
            }

            if (currentConfigDescriptor instanceof ViewConfigDescriptor && Arrays.asList(currentConfigDescriptor.getConfigClass().getInterfaces()).contains(parentConfigDescriptor.getConfigClass())) {
                subMenuItems.add((ViewConfigDescriptor) currentConfigDescriptor);
            }
        }

        subMenuItems.sort(Comparator.comparingInt(cd -> cd.getConfigClass().getAnnotation(MenuEntry.class).pos()));

        for (ViewConfigDescriptor menuItemDescriptor : subMenuItems) {
            DefaultMenuItem menuItem = new DefaultMenuItem(menuItemDescriptor.getConfigClass().getSimpleName());
            menuItem.setAjax(false);
            menuItem.setOutcome(menuItemDescriptor.getViewId());
            menuItem.setDisableClientWindow(false);
            menuItem.setImmediate(true);
            View view = menuItemDescriptor.getMetaData(View.class).iterator().next(); //@View can't get aggregated, but there is always at least an implicit one
            menuItem.setIncludeViewParams(view.viewParams() == View.ViewParameterMode.INCLUDE);
            parentMenuEntry.addElement(menuItem);
        }
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }
}
