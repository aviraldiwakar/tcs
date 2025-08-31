import java.util.*;

public class Banking {

    private enum TransactionType {
        CREDIT, DEBIT
    }

    private static class Transaction {
        TransactionType type;
        double amount;
        boolean isCommitted;

        public Transaction(TransactionType type, double amount) {
            this.type = type;
            this.amount = amount;
            this.isCommitted = false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read initial balance
        double currentBalance = sc.nextDouble();
        sc.nextLine();

        // List to store all credit and debit
        List<Transaction> transactions = new ArrayList<>();
        // List to store the balance at each commit point
        List<Double> committedBalances = new ArrayList<>();
        // List to store the total number of transactions at each commit point
        List<Integer> transactionsAtCommit = new ArrayList<>();

        int transactionCount = 0;

        String operation;
        do {
            operation = sc.next();

            switch (operation) {
                case "read":
                    System.out.println(String.format("%.0f", currentBalance));
                    break;

                case "credit":
                    double creditAmount = sc.nextDouble();
                    currentBalance += creditAmount;
                    transactions.add(new Transaction(TransactionType.CREDIT, creditAmount));
                    transactionCount++;
                    break;

                case "debit":
                    double debitAmount = sc.nextDouble();
                    currentBalance -= debitAmount;
                    transactions.add(new Transaction(TransactionType.DEBIT, debitAmount));
                    transactionCount++;
                    break;

                case "commit":
                    // transactions as committed
                    for (int j = 0; j < transactions.size(); j++) {
                        transactions.get(j).isCommitted = true;
                    }
                    // Save the current state
                    committedBalances.add(currentBalance);
                    transactionsAtCommit.add(transactions.size());
                    break;

                case "abort":
                    int abortIndex = sc.nextInt();
                    // Abort is 1-indexed, so convert to 0-indexed
                    if (abortIndex > 0 && abortIndex <= transactions.size()) {
                        Transaction toAbort = transactions.get(abortIndex - 1);
                        if (toAbort.isCommitted) {
                            System.out.println("Aborted transaction has been committed");
                        } else {
                            // Reverse the transaction's effect on the current balance
                            if (toAbort.type == TransactionType.CREDIT) {
                                currentBalance -= toAbort.amount;
                            } else {
                                currentBalance += toAbort.amount;
                            }
                            // Remove the transaction from the list
                            transactions.remove(abortIndex - 1);
                        }
                    }
                    break;

                case "rollback":
                    int rollbackIndex = sc.nextInt();
                    if (rollbackIndex > 0 && rollbackIndex <= committedBalances.size()) {
                        // Rollback to the balance at the specified commit point
                        currentBalance = committedBalances.get(rollbackIndex - 1);

                        // Adjust the transaction and commit history lists
                        // Retain only the commits up to the rollback point
                        committedBalances = new ArrayList<>(committedBalances.subList(0, rollbackIndex));
                        transactionsAtCommit = new ArrayList<>(transactionsAtCommit.subList(0, rollbackIndex));

                        // Find the number of transactions to keep from the history
                        int transactionsToKeep = transactionsAtCommit.get(rollbackIndex - 1);
                        // Retain only the transactions up to the rollback point
                        transactions = new ArrayList<>(transactions.subList(0, transactionsToKeep));

                    }
                    break;

                default:
                    // In case of an unexpected operation
                    System.err.println("Unknown operation: " + operation);
                    break;
            }
        } while (operation == "end");
        sc.close();
    }
}