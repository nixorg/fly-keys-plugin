package com.nixorg.idea.fly;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.event.EditorFactoryAdapter;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.FocusWatcher;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.testFramework.LightVirtualFile;
import com.nixorg.idea.fly.action.CommandMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.intellij.icons.AllIcons.Nodes.Jsf.Component;

public class FlyPlugin implements ApplicationComponent {

    private static final Logger log = Logger.getInstance(FlyPlugin.class);
    private static boolean enable = true;

    public FlyPlugin() {

    }

    public static void setEnable(boolean enable)
    {
        FlyPlugin.enable = enable;
    }

    public static boolean isEnable()
    {
        return enable;
    }

    @Override
    public void initComponent() {

        final TypedAction typedAction = EditorActionManager.getInstance().getTypedAction();
        typedAction.setupRawHandler(new FlyTypedActionHandler(typedAction.getRawHandler()));

        ApplicationManager.getApplication().getComponent(EditorFactory.class).addEditorFactoryListener(new EditorFactoryAdapter() {
            @Override
            public void editorCreated(EditorFactoryEvent event) {
                Editor editor = event.getEditor();

                if (!isDialogWindow(editor.getProject()) && isFileEditor(editor)) {
                    switchToCommandMode(editor);
                }

                CommandMode.getInstance().registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)), editor.getComponent());
            }
        }, ApplicationManager.getApplication());

    }

    public static void switchToCommandMode(Editor editor) {
        FlyKeyHandler.getInstance().mode = FlyKeyHandler.Mode.COMMOND;
        log.info("Command " + String.valueOf(FlyKeyHandler.getInstance().mode));
        FlyPlugin.showMessage("COMMAND");
        editor.getSettings().setBlockCursor(true);
    }

    public static void switchToInsertMode(Editor editor) {
        FlyKeyHandler.getInstance().mode = FlyKeyHandler.Mode.COMMOND;
        log.info("Command " + String.valueOf(FlyKeyHandler.getInstance().mode));
        FlyPlugin.showMessage("INSERT");
        editor.getSettings().setBlockCursor(false);
    }

    public static boolean isDialogWindow(Project project)
    {
        Window window = WindowManager.getInstance().suggestParentWindow(project);
        return window instanceof Dialog;
    }

    public static boolean isFileEditor(Editor editor){
        final VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        return virtualFile != null && !(virtualFile instanceof LightVirtualFile);
    }


    public static void showMessage(@Nullable String msg) {
        ProjectManager pm = ProjectManager.getInstance();
        Project[] projects = pm.getOpenProjects();
        for (Project project : projects) {
            StatusBar bar = WindowManager.getInstance().getStatusBar(project);
            if (bar != null) {
                bar.setInfo(msg);
            }
        }
    }

}
