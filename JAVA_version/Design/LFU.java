// 460. LFU Cache

/*
Two HashMaps are used, one to store <key, value> pair, another store the <key, node>.
I use double linked list to keep the frequent of each key. In each double linked list node, keys with the same count are saved using java built in LinkedHashSet. This can keep the order.
Every time, one key is referenced, first find the current node corresponding to the key, If the following node exist and the frequent is larger by one, add key to the keys of the following node, else create a new node and add it following the current node.
All operations are guaranteed to be O(1).
*/

class LFUCache {
    
    class Node {
        int count = 0;
        LinkedHashSet<Integer> keys;
        Node prev;
        Node next;
        
        public Node(int c) {
            count = c;
            keys = new LinkedHashSet<>();
            prev = null;
            next = null;
        }
    }
    
    Node head = null;
    int cap;
    Map<Integer, Integer> valueHash;
    Map<Integer, Node> nodeHash;

    public LFUCache(int capacity) {
        cap = capacity;
        valueHash = new HashMap<>();
        nodeHash = new HashMap<>();
    }
    
    public int get(int key) {
        if (valueHash.containsKey(key)) {
            increaseCount(key);
            return valueHash.get(key);
        }
        return -1;
    }
    
    public void put(int key, int value) {
        if (cap == 0)
            return;
        
        if (valueHash.containsKey(key)) {
            valueHash.put(key, value);
            increaseCount(key);
            return;
        }
        
        if (valueHash.size() < cap) {
            valueHash.put(key, value);
        } else {
            removeOld();
            valueHash.put(key, value);
        }
        
        addToHead(key);
        increaseCount(key);
    }
    
    private void addToHead(int key) {
        if (head == null) {
            head = new Node(0);
            head.keys.add(key);
        } else if (head.count > 0) {
            Node node = new Node(0);
            node.keys.add(key);
            node.next = head;
            head.prev = node;
            head = node;
        } else {
            head.keys.add(key);
        }
        
        nodeHash.put(key, head);
    }
    
    private void increaseCount(int key) {
        Node node = nodeHash.get(key);
        node.keys.remove(key);
        
        if (node.next == null) {
            node.next = new Node(node.count + 1);
            node.next.prev = node;
            node.next.keys.add(key);
        } else if (node.next.count == node.count + 1) {
            node.next.keys.add(key);
        } else {
            Node tmp = new Node(node.count + 1);
            tmp.keys.add(key);
            tmp.prev = node;
            tmp.next = node.next;
            node.next.prev = tmp;
            node.next = tmp;
        }
        
        nodeHash.put(key, node.next);
        if (node.keys.size() == 0)
            remove(node);
    }
    
    private void removeOld() {
        if (head == null)
            return;
        
        int old = 0;
        for (int n : head.keys) {
            old = n;
            break;
        }
        
        head.keys.remove(old);
        if (head.keys.size() == 0)
            remove(head);
        nodeHash.remove(old);
        valueHash.remove(old);
    }
    
    private void remove(Node node) {
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */




// approach 2
class LFUCache {
    Map<Integer, Integer> vals;
    Map<Integer, Integer> counts;
    Map<Integer, LinkedHashSet<Integer>> lists;
    int min = -1;
    int cap;

    public LFUCache(int capacity) {
        cap = capacity;
        vals = new HashMap<>();
        counts = new HashMap<>();
        lists = new HashMap<>();
        lists.put(1, new LinkedHashSet<>());
    }
    
    public int get(int key) {
        if (!vals.containsKey(key))
            return -1;
        
        int count = counts.get(key);
        counts.put(key, count + 1);
        lists.get(count).remove(key);
        if (count == min && lists.get(count).size() == 0)
            min++;
        if (!lists.containsKey(count + 1))
            lists.put(count + 1, new LinkedHashSet<>());
        lists.get(count + 1).add(key);
        
        return vals.get(key);
    }
    
    public void put(int key, int value) {
        if (cap == 0)
            return;
        
        if (vals.containsKey(key)) {
            vals.put(key, value);
            get(key);
            return;
        }
        
        if (vals.size() >= cap) {
            int evictKey = lists.get(min).iterator().next();
            lists.get(min).remove(evictKey);
            vals.remove(evictKey);
            counts.remove(evictKey);
        }
        
        vals.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */