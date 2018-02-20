package com.nixorg.idea.fly.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.nixorg.idea.fly.FlyKeyHandler;
import com.nixorg.idea.fly.FlyPlugin;

public class InsertMode extends AnAction {
    private static final Logger log = Logger.getInstance(InsertMode.class);


    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        FlyKeyHandler.getInstance().mode = FlyKeyHandler.Mode.INSERT;
        FlyPlugin.showMessage("INSERT");
        editor.getSettings().setBlockCursor(false);
        log.info("insert " + String.valueOf(FlyKeyHandler.getInstance().mode));

    }
}
