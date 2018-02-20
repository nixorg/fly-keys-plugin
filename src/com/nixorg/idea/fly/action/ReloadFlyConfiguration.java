package com.nixorg.idea.fly.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nixorg.idea.fly.FlyKeyHandler;

public class ReloadFlyConfiguration extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        FlyKeyHandler.getInstance().readFlyConfiguration();
    }
}
