package com.nixorg.idea.fly.action;

import com.intellij.codeInsight.navigation.IncrementalSearchHandler;
import com.intellij.ide.impl.DataManagerImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.ui.LightweightHint;
import com.nixorg.idea.fly.FlyKeyHandler;
import com.nixorg.idea.fly.FlyPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class ChangeCharAction extends AnAction {
    private static final Logger log = Logger.getInstance(InsertMode.class);

    private static KeyAdapter keyListener = null;

    @Override
    public void actionPerformed(AnActionEvent e)
    {
        Project project = e.getData(CommonDataKeys.PROJECT);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        String keySequence = ((DataManagerImpl.MyDataContext) e.getDataContext()).getUserData(FlyKeyHandler.FLY_KEY_SEQUENCE);

        final Document document = editor.getDocument();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.replaceString(editor.getCaretModel().getOffset(),editor.getCaretModel().getOffset()+1,keySequence);
            }
        };

        WriteCommandAction.runWriteCommandAction(project, runnable);
    }
}
