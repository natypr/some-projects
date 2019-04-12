package by.naty.chat.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

class Story {
    private LinkedList<String> story = new LinkedList<>();

    void addStory(String el) {
        if (story.size() >= 5) {
            story.removeFirst();
        }
        story.add(el);
    }

    /**
     * Send messages from the list to the output stream to the user.
     */
    void printStory(BufferedWriter writer) {
        if (story.size() > 0) {
            try {
                writer.write("History" + "\n");
                for (String m : story) {
                    writer.write(m + "\n");
                }
                writer.write("---" + "\n");
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }
}
