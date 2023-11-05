package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.taskMap.containsKey(task.getId())) {
                remove(task.getId());
            }
            history.taskMap.put(task.getId(), history.linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        if(history.taskMap.containsKey(id)) {
            CustomLinkedListNode delete = history.taskMap.remove(id);
            history.removeNode(delete);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    private static class CustomLinkedList {

        public CustomLinkedListNode first;
        public CustomLinkedListNode last;
        public Map<Integer, CustomLinkedListNode> taskMap = new HashMap<>();


        private CustomLinkedListNode linkLast(Task data) {
            CustomLinkedListNode newNode = new CustomLinkedListNode(data);
            if (first == null) {
                first = newNode;
                last = newNode;
                newNode.next = null;
                newNode.prev = null;
            } else {
                newNode.prev = last;
                newNode.next = null;
                last.next = newNode;
                last = newNode;
            }
            return newNode;
        }

        private List<Task> getTasks() {
            List<Task> allTask = new ArrayList<>();
            CustomLinkedListNode current = first;
            while (current != null) {
                allTask.add(current.task);
                current = current.next;
            }
            return allTask;
        }

        private void removeNode(CustomLinkedListNode delNode) {
            if (delNode.prev == null) {
                first = delNode.next;
            }
            if (delNode.next == null) {
                last = delNode.prev;
            }
            if (delNode.next != null) {
                delNode.next.prev = delNode.prev;
            }
            if (delNode.prev != null) {
                delNode.prev.next = delNode.next;
            }
        }
    }
}