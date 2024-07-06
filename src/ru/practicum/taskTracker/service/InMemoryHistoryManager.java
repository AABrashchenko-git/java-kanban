package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    static class HistoryLinkedList {
        private Node head;
        private Node tail;
        private final Map<Integer, Node> historyMap = new HashMap<>();

        private void linkLast(Task task) {
            if (historyMap.containsKey(task.getId())) {
                removeNode(historyMap.get(task.getId()));
            }
            Node oldTail = tail;
            Node newNode = new Node(task, tail, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }
            historyMap.put(task.getId(), newNode);
        }

        private void removeNode(Node nodeToRemove) {
            if (nodeToRemove == tail && nodeToRemove == head) {
                tail = null;
                head = null;
            } else if (nodeToRemove.getNext() == null) { // Если узел - конец
                tail = nodeToRemove.getPrev();
                tail.setNext(null);
            } else if (nodeToRemove.getPrev() == null) { // Если узел - начало
                head = nodeToRemove.getNext();
                head.setPrev(null);
            } else {
                nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
                nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
            }
        }

        private List<Task> getTasks() {
            List<Task> taskHistory = new LinkedList<>();
            Node currentNode = head;
            while (currentNode != null) {
                taskHistory.add(currentNode.getTaskOfNode());
                currentNode = currentNode.getNext();
            }
            return taskHistory;
        }

        public Map<Integer, Node> getHistoryMap() {
            return historyMap;
        }
    }

    HistoryLinkedList historyLinkedList = new HistoryLinkedList();

    @Override
    public void add(Task task) {
        historyLinkedList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = historyLinkedList.getHistoryMap().remove(id);
        if (nodeToRemove == null) {
            return;
        }
        historyLinkedList.removeNode(nodeToRemove);
    }

}

