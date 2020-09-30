package com.r3235.server;

import com.r3235.Connector.UdpReader;
import com.r3235.Connector.UdpSender;
import com.r3235.tools.CommandType;
import com.r3235.tools.Response;
import com.r3235.tools.Task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

/**
 * Main-класс
 */
public class MainConsole {
    /**
     * Стартовая точка программы. Считывает путь к файлу из переменной окружения и запускает обработчик команд.
     *
     * @param args Аргументы командной строки (не спользуются)
     */
    static TaskWorker taskWorker;

    public static void main(String[] args) {
        System.out.println("Проверка переменой окружения LabFile");
        String file = System.getenv("LabFile");
        System.out.println("Путь, заданный в переменной окружения LabFile: " + file);
        if (file == null) {
            System.out.println("Переменная окружения LabFile не была задана.");
            return;
        }
        System.out.println("Введите порт , на котором будет работать сервер или 'exit' для выхода");
        int port = -1;
        while (port == -1) {
            Scanner scanner = new Scanner(System.in);
            if (!scanner.hasNextLine()) {
                System.exit(0);
            }
            String inputString = scanner.nextLine();
            if (inputString.equals("exit")) {
                System.exit(0);
            } else {
                try {
                    port = Integer.parseInt(inputString);
                    if (port < 0 || port > 65535) {
                        System.out.println("Порт - число от 0 до 65535.");
                        port=-1;
                    }
                } catch (Exception e) {
                    System.out.println("Некорректный ввод.");
                }
            }
        }
        System.out.println("Загрузка коллекции из файла: " + file);
        CollectionControl collectionControl = new CollectionControl();
        try {
            if (collectionControl.loadCollection(file)) {
                System.out.println("Запуск сервера:");
                System.out.println("Создание канала...");
                DatagramChannel datagramChannel = DatagramChannel.open();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
                datagramChannel.bind(inetSocketAddress);
                System.out.println("Создание отправщика...");
                UdpSender udpSender = new UdpSender(datagramChannel);
                System.out.println("Создание приёмника...");
                UdpReader udpReader = new UdpReader(datagramChannel);
//обработчик ответов сервера
                TaskWorker taskWorker = new TaskWorker(collectionControl);
                udpReader.setExecutor((data, inputAddress) -> {
                    try (
                            ObjectInputStream objectInputStream = new ObjectInputStream(
                                    new ByteArrayInputStream(data))
                    ) {
                        Task task = (Task) objectInputStream.readObject();
                        objectInputStream.close();
                        if (task != null) {
                            System.out.println("Новая команда '" + task.getType() + "' от " + inputAddress);
                            if (task.getType() != null ) {
                                Response response;
                                if(task.getType() == CommandType.SAVE)
                                    response = new Response("Команда доступна только на стороне сервера.");
                                else response = taskWorker.executeTask(task);
                                udpSender.send(response, inputAddress);
                            }
                        } else System.out.println("Сообщение клиента " + inputAddress + " пустое.");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Ошибка десериализации сообщения клиента.");
                    }
                });
                System.out.println("Запуск обработчика...");
                udpReader.startListening();

                Scanner scanner = new Scanner(System.in);
                System.out.print("->>");
                while (scanner.hasNext()) {
                    String stringTask = scanner.nextLine();
                    if (stringTask.equals("exit")) break;
                    else if (stringTask.equals("save"))
                        System.out.println(taskWorker.executeTask(new Task(CommandType.SAVE)).getMsg());
                    else System.out.println("Доступные комадны сервера: 'save', 'exit'.");
                    System.out.print("->>");
                }
            } else {
                System.out.println("Увы...");
            }
        } catch (SocketException e) {
            System.out.println("Ошибка создагия сокета. Возможно, порт занят.");
        } catch (Exception e) {
            System.out.println("Да, тут случилась какая-то ошибка. Пожалуйста, примите выполнение.");
        }
        System.out.println("Работа программы завершена");
    }
}

