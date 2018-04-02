package org.os890.ds;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

public interface Pages extends ViewConfig {
    class Index implements Pages {}

    @MenuEntry(pos = 1)
    interface Section1 extends Pages {
        @MenuEntry(pos = 1)
        class Content1 implements Section1 {}

        @MenuEntry(pos =2)
        class Content2 implements Section1 {}
    }

    @MenuEntry(pos = 2)
    interface Section2 extends Pages {
        @MenuEntry(pos = 1)
        class Content1 implements Section2 {}

        @MenuEntry(pos =2)
        class Content2 implements Section2 {}
    }

    @MenuEntry(pos = 3)
    interface Section3 extends Pages {
        @MenuEntry(pos = 1)
        class Content1 implements Section3 {}

        @MenuEntry(pos =2)
        class Content2 implements Section3 {}
    }
}
