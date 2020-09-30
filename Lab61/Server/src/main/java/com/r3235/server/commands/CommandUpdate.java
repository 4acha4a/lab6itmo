package com.r3235.server.commands;

import com.r3235.collection.Product;
import com.r3235.tools.CommandType;
import com.r3235.tools.Response;
import com.r3235.server.CollectionControl;
import com.r3235.tools.Task;

public class CommandUpdate implements Command {
    @Override
    public Response executeTask(CollectionControl collectionControl, Task task) {
        String msg;
        Product product = (Product) task.getFirstArgument();
        int id = (product.getId());
        Command comr = new CommandRemoveById();
        int startSize = collectionControl.getCollection().size();
        comr.executeTask(collectionControl, new Task(CommandType.REMOVE_BY_ID, id));
        if (startSize == collectionControl.getCollection().size()) msg = "Элемента с id " + id + " не существует";
        else {
            collectionControl.getCollection().add(product);
            msg = "Элемент изменён";
        }
        return new Response(msg);
    }

}
