package com.r3235.server.commands;

import com.r3235.tools.Response;
import com.r3235.server.CollectionControl;
import com.r3235.tools.Task;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CommandRemoveById implements Command {
    @Override
    public Response executeTask(CollectionControl collectionControl, Task task) {
        String msg;
        int startSize = collectionControl.getCollection().size();
        collectionControl.getCollection().removeAll(collectionControl.getCollection().stream()
                .filter(product -> product.getId() == (long) task.getFirstArgument())
                .collect(Collectors.toCollection(ArrayList::new)));
        if (startSize == collectionControl.getCollection().size())
            msg = "Элемент коллекции не был удалён.Возможно, он не существует.";
        else msg = "Элемент коллекции был удалён.";
        return new Response(msg);
    }
}
