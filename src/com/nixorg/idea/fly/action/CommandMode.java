package com.nixorg.idea.fly.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.nixorg.idea.fly.FlyKeyHandler;
import com.nixorg.idea.fly.FlyPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandMode extends AnAction {
    private static final Logger log = Logger.getInstance(CommandMode.class);

    private static AnAction instance;

    public static AnAction getInstance() {
        if (instance == null) {
            final AnAction originalAction = ActionManager.getInstance().getAction("CommandMode");
            instance = EmptyAction.wrap(originalAction);
        }
        return instance;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        FlyKeyHandler.getInstance().mode = FlyKeyHandler.Mode.COMMOND;
        log.info("Command " + String.valueOf(FlyKeyHandler.getInstance().mode));
        FlyPlugin.showMessage("COMMAND");
        editor.getSettings().setBlockCursor(true);


        //final Project project = e.getData(CommonDataKeys.PROJECT);
        //final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final AnAction escape = ActionManager.getInstance().getAction("EditorEscape");
        escape.actionPerformed(e);
        /*if(editor != null){
            final AnAction originalAction = ActionManager.getInstance().getAction("Left");
            final AnAction twoAction = ActionManager.getInstance().getAction("Right");
            final AnAction upAction = ActionManager.getInstance().getAction("EditorUp");
            upAction.registerCustomShortcutSet(KeyEvent.VK_C,0,editor.getComponent()  );
            originalAction.registerCustomShortcutSet(KeyEvent.VK_H,0,editor.getComponent()  );
            twoAction.registerCustomShortcutSet(KeyEvent.VK_N,0,editor.getComponent()  );

        }
        else {
            final Editor editor1 = e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE);
            final AnAction originalAction1 = ActionManager.getInstance().getAction("Left");
            final AnAction twoAction1 = ActionManager.getInstance().getAction("Right");
            final AnAction upAction1 = ActionManager.getInstance().getAction("EditorUp");
            final AnAction escape = ActionManager.getInstance().getAction("CloseAction");

            //upAction1.unregisterCustomShortcutSet(editor1.getComponent());
            //originalAction1.unregisterCustomShortcutSet(editor1.getComponent());
            //twoAction1.unregisterCustomShortcutSet(editor1.getComponent());
            //escape.actionPerformed(e);
        }*/
    }
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(isEnable(e));
    }



    private boolean isEnable(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (editor == null || FlyPlugin.isDialogWindow(e.getProject()) || editor.getEditorKind().name().equals("DIFF")){
            return false;
        }
        return true;
    }

}
