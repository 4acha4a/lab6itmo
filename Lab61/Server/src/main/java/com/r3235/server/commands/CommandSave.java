package com.r3235.server.commands;

import com.r3235.tools.Response;
import com.r3235.server.CollectionControl;
import com.r3235.tools.Task;

public class CommandSave implements Command {
    @Override
    public Response executeTask(CollectionControl collectionControl, Task task) {
        if (collectionControl.saveCollection())
            return new Response("Коррекция успешно сохранена");
        else return new Response("Ошибка сохранения коллекции");
    }
}
