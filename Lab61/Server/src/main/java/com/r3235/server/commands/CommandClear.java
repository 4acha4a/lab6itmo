package com.r3235.server.commands;

import com.r3235.tools.Response;
import com.r3235.server.CollectionControl;
import com.r3235.tools.Task;

public class CommandClear implements Command {
    @Override
    public Response executeTask(CollectionControl collectionControl, Task task) {
        String msg;
        if (collectionControl.getCollection().size() == 0) {
            msg = "Коллекция пуста";
        } else {
            collectionControl.getCollection().clear();
            msg = "Коллекция очищена";
        }
        return new Response(msg);
    }
}
