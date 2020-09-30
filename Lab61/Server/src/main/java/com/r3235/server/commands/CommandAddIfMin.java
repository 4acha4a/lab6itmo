package com.r3235.server.commands;

import com.r3235.tools.CommandType;
import com.r3235.collection.Product;
import com.r3235.tools.Response;
import com.r3235.server.CollectionControl;
import com.r3235.tools.Task;

import java.util.LinkedList;

public class CommandAddIfMin implements Command {
    @Override
    public Response executeTask(CollectionControl collectionControl, Task task) {

            LinkedList<Product> products = collectionControl.getCollection();
                if (products.size() != 0) {
                    Product addProduct = (Product) task.getFirstArgument();
                    Product minElem = products.stream().min(Product::compareTo).get();
                    if (addProduct.compareTo(minElem) < 0) {
                        Command add = new CommandAdd();
                        return  add.executeTask(collectionControl,new Task(CommandType.ADD, addProduct));
                    } else return new Response("Элемент не минимальный!");
                } else return new Response("Коллекция пуста, минимальный элемент отсутствует.");

    }

}
