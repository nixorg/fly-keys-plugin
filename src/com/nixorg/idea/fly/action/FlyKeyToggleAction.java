package com.nixorg.idea.fly.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.nixorg.idea.fly.FlyPlugin;

public class FlyKeyToggleAction extends ToggleAction implements DumbAware {

    @Override
    public boolean isSelected(AnActionEvent e)
    {
        return FlyPlugin.isEnable();
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state)
    {
        FlyPlugin.setEnable(state);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (state) {
            FlyPlugin.switchToCommandMode(editor);
        } else {
            FlyPlugin.switchToInsertMode(editor);
        }
    }
}
