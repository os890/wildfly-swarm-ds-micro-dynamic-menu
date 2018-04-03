package org.os890.pf;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.jsf.api.config.view.View;
import org.os890.ds.Labels;
import org.os890.ds.MenuEntry;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Named
@WindowScoped
public class MenuModelProvider implements Serializable {
    private MenuModel menuModel = new DefaultMenuModel();

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    private Labels labels;

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
            String label = labels.getSectionLabel(configDescriptor.getConfigClass());
            DefaultSubMenu subMenu = new DefaultSubMenu(label);
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
            Class<? extends ViewConfig> subMenuConfig = menuItemDescriptor.getConfigClass();
            String label = labels.getSubMenuLabel(parentConfigDescriptor.getConfigClass(), subMenuConfig);

            DefaultMenuItem menuItem = new DefaultMenuItem(label);
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
