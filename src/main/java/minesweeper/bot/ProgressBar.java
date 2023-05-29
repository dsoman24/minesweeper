package src.main.java.minesweeper.bot;

public class ProgressBar {
    public static void updateProgressBar(int currentTask, int totalTasks) {
        int progress = (int) (currentTask / (double) totalTasks * 100);
        int length = 50; // Length of the progress bar

        StringBuilder progressBar = new StringBuilder();
        progressBar.append('[');

        int completedSymbols = (int) (length * (progress / 100.0));
        for (int i = 0; i < length; i++) {
            if (i <= completedSymbols) {
                progressBar.append('=');
            } else {
                progressBar.append(' ');
            }
        }

        progressBar.append(']');
        progressBar.append("  " + progress + "%");

        System.out.print("\r" + progressBar.toString()); // Print the progress bar on the same line
        System.out.flush();
    }
}
