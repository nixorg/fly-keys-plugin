package com.nixorg.idea.fly;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.intellij.ide.impl.DataManagerImpl;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Key;
import org.jetbrains.jps.incremental.GlobalContextKey;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Timer;

public class FlyKeyHandler {
    private static final Logger log = Logger.getInstance(FlyKeyHandler.class);

    public static final Key<String> FLY_KEY_SEQUENCE = GlobalContextKey.create("fly.key.sequence");

    public static final String SPACE_KEY = " ";

    private Map<String, String> actionMap;

    private String keyHistory = "";

    private Timer timer;

    private static FlyKeyHandler instance;

    public enum Mode {
        COMMOND,INSERT;
    }

    public Mode mode;

    private FlyKeyHandler () {
        readFlyConfiguration();
    }

    public void readFlyConfiguration()
    {
        Type mapType = new TypeToken<Map<String, String>>(){}.getType();

        String json = null;
        try {
            File file = new File(System.getProperty("user.home"),(".fly_actions"));
            json = new String (Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionMap = new Gson().fromJson(json, mapType);
    }

    public static FlyKeyHandler getInstance() {
        if (instance == null) {
            instance = new FlyKeyHandler();
        }
        return instance;
    }

    // false - default behavior
    // true  - do nothing
    public boolean handleKey(Editor editor, KeyStroke keyStroke, DataContext dataContext) {

        String keySequence = String.valueOf(keyStroke.getKeyChar());

        ((DataManagerImpl.MyDataContext) dataContext).putUserData(FLY_KEY_SEQUENCE, keySequence);

        log.info("here is the key:"+ keyHistory);

        if (editor.isOneLineMode() || FlyKeyHandler.getInstance().mode == Mode.INSERT) {
            log.info("set " + String.valueOf(FlyKeyHandler.getInstance().mode));
            return false;
        }

        setupTimer();
        addKeyToHistory(keySequence);

        return executeActionByName(dataContext, getActionByKeySequence(keyHistory), editor);
    }

    private String getActionByKeySequence(String keySequence) {
        String action = actionMap.get(getKeySequence(keySequence));

        if (action == null) {
            for (String s : actionMap.keySet()) {
                if (!s.equals(".") && keySequence.matches(s)) {
                    action = actionMap.get(s);
                    break;
                }
            }
        }
        return action;
    }

    private void setupTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearKeyHistory();
                log.info("clear");
            }
        },2000);
    }

    private String getKeySequence(String keySequence) {
        String key;
        if (!keyHistory.isEmpty()){
            key = keyHistory;
        }
        else {
            key = keySequence;
        }
        return key;
    }

    private void addKeyToHistory(String keySequence) {
        keyHistory += keySequence;
    }

    private void clearKeyHistory() {
        keyHistory = "";
    }

    private boolean executeActionByName(DataContext dataContext, String actionToHandle, Editor editor) {

        if (actionToHandle == null) {
            return true;
        }

        String[] actionsArray = actionToHandle.split("->");
        for (String actionName : actionsArray) {
            AnAction action = ActionManager.getInstance().getAction(actionName);
            AnActionEvent event = new AnActionEvent(null, dataContext, "", action.getTemplatePresentation(),
                    ActionManager.getInstance(), 0);
            action.actionPerformed(event);
        }
        clearKeyHistory();
        timer.cancel();

        return true;
    }
}
