import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Order_It {

    //to find the length
    private static int getLisLength(List<Integer> list) {
        if (list.isEmpty()) {
            return 0;
        }

        int n = list.size();
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        int maxLength = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (list.get(i) > list.get(j)) {
                    dp[i] = Math.max(dp[i], 1 + dp[j]);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = Integer.parseInt(sc.nextLine());

        if (N == 0) {
            System.out.println(0);
            return;
        }

        // Read list
        sc.nextLine();
        String[] shuffledList = new String[N];
        for (int i = 0; i < N; i++) {
            shuffledList[i] = sc.nextLine();
        }

        // Read original
        sc.nextLine();
        Map<String, Integer> targetMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            targetMap.put(sc.nextLine(), i);
        }

        // 1
        int[] P = new int[N];
        for (int i = 0; i < N; i++) {
            P[i] = targetMap.get(shuffledList[i]);
        }

        // 2
        List<Integer> B = new ArrayList<>();
        B.add(P[0]);

        for (int i = 1; i < N; i++) {
            if (P[i] != P[i-1] + 1) {
                B.add(P[i]);
            }
        }

        int k = B.size();

        // 3
        int L_B = getLisLength(B);

        // 4
        System.out.println(k - L_B);

        sc.close();
    }
}
