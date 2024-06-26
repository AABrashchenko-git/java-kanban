package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    static class Node<T> {
        Task task;
        Node<T> prev;
        Node<T> next;

        public Node(Task task, Node<T> prev, Node<T> next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTaskOfNode() {
            return task;
        }

    }

    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeToRemove = historyMap.remove(id);
        if (nodeToRemove == null) {
            return;
        }
        removeNode(nodeToRemove);
    }

    public void linkLast(Task task) {
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    private void removeNode(Node<Task> nodeToRemove) {
        if (nodeToRemove == tail && nodeToRemove == head) {
            tail = null;
            head = null;
        } else if (nodeToRemove.next == null) { // Если узел - конец
            tail = nodeToRemove.prev;
            tail.next = null;
        } else if (nodeToRemove.prev == null) { // Если узел - начало
            head = nodeToRemove.next;
            head.prev = null;
        } else {
            nodeToRemove.prev.next = nodeToRemove.next;
            nodeToRemove.next.prev = nodeToRemove.prev;
        }
    }

    private List<Task> getTasks() {
        List<Task> taskHistory = new LinkedList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            taskHistory.add(currentNode.getTaskOfNode());
            currentNode = currentNode.next;
        }
        return taskHistory;
    }

}

