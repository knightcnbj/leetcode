// 378. Kth Smallest Element in a Sorted Matrix

class Solution {
    int[] dx = new int[]{0, 1};
    int[] dy = new int[]{1, 0};

    class Point {
        int x;
        int y;
        int val;
        
        Point(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }
    }
    
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int m = matrix[0].length;
        Queue<Point> minHeap = new PriorityQueue<>((a, b) -> (a.val - b.val));
        minHeap.offer(new Point(0, 0, matrix[0][0]));
        boolean[][] visited = new boolean[n][m];
        visited[0][0] = true;
        
        for (int i = 0; i < k - 1; i++) {
            Point curr = minHeap.poll();
            for (int j = 0; j < 2; j++) {
                int nx = curr.x + dx[j];
                int ny = curr.y + dy[j];
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    minHeap.offer(new Point(nx, ny, matrix[nx][ny]));
                }
            }
        }
        
        return minHeap.peek().val;
    }
}



// binary search 
class Solution {
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int m = matrix[0].length;
        
        int low = matrix[0][0];
        int high = matrix[n - 1][m - 1] + 1;
        
        while (low < high) {
            int mid = low + (high - low) / 2;
            int count = 0;
            int j = m - 1;
            
            for (int i = 0; i < n; i++) {
                while (j >= 0 && matrix[i][j] > mid) {
                    j--;
                }
                count += (j + 1);
            }
            if (count < k)
                low = mid + 1;
            else
                high = mid;
        }
        
        return low;
    }
}