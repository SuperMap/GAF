/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.develop.ui.DialogLoginGaf;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionLoginGaf extends CtrlAction {
    public static boolean enable = true;

    public CtrlActionLoginGaf(IBaseItem caller) {
		super(caller);
	}


    @Override
    public boolean enable() {
        return enable;
    }

    @Override
	public void run() {
        if(enable()){
//            SmCommandMenuButton iBaseItem = (SmCommandMenuButton) getCaller();
            new DialogLoginGaf().showDialog();
        }
	}
}
