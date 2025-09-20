package ru.common.manager;

import ru.common.interfaces.HistoryManager;
import ru.common.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> nodeMap;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.nodeMap = new HashMap<>();
    }

    public static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + prev +
                    ", next=" + next +
                    '}';
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        // Если задача уже есть — удаляем её из списка
        Node node = nodeMap.remove(task.getId());
        if (node != null) {
            removeNode(node);
        }

        // Вставляем задачу в конец списка
        Node newNode = linkLast(task);

        // Запоминаем в HashMap
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }


    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node == head && node == tail) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = node.next;
            head.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else { // middle
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    public Node linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(task, oldTail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode; // список был пуст
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    public Map<Integer, Node> getNodeMap() {
        return nodeMap;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }
}
