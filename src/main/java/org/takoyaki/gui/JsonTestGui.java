package org.takoyaki.gui;

import org.takoyaki.json.JsonApi;
import org.takoyaki.json.JsonMethod;
import org.takoyaki.util.JsonMapper;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.util.Objects;

public final class JsonTestGui {
    private static final String WINDOW_TITLE = "JSON API Test";
    private static final String ENDPOINT_LABEL = "Endpoint";
    private static final String SEND_LABEL = "Send";
    private static final String REQUEST_LABEL = "Request JSON";
    private static final String RESPONSE_LABEL = "Response JSON";
    private static final String ERROR_TITLE = "Request Error";
    private static final String EMPTY_ENDPOINT_ERROR = "Endpoint cannot be empty";
    private static final String DEFAULT_ENDPOINT = "/";
    private static final String DEFAULT_JSON = "{}";
    private static final String SENDING_MESSAGE = "Sending...";
    private static final int WINDOW_WIDTH = 760;
    private static final int WINDOW_HEIGHT = 520;
    private static final int ENDPOINT_COLUMNS = 32;
    private static final int BORDER_SIZE = 8;
    private static final double DIVIDER_POSITION = 0.5;

    private JsonTestGui() {
    }

    public static void open(JsonApi api) {
        Objects.requireNonNull(api, "api cannot be null");
        if (GraphicsEnvironment.isHeadless()) {
            throw new IllegalStateException("The JSON test GUI cannot be opened in a headless environment");
        }
        SwingUtilities.invokeLater(() -> createFrame(api).setVisible(true));
    }

    private static JFrame createFrame(JsonApi api) {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setLocationByPlatform(true);

        JComboBox<JsonMethod> methodBox = new JComboBox<>(JsonMethod.values());
        JTextField endpointField = new JTextField(DEFAULT_ENDPOINT, ENDPOINT_COLUMNS);
        JButton sendButton = new JButton(SEND_LABEL);

        JPanel requestHeader = new JPanel(new FlowLayout(FlowLayout.LEADING));
        requestHeader.add(methodBox);
        requestHeader.add(new JLabel(ENDPOINT_LABEL));
        requestHeader.add(endpointField);
        requestHeader.add(sendButton);

        JTextArea requestArea = createTextArea(DEFAULT_JSON, true);
        JTextArea responseArea = createTextArea("", false);
        JPanel requestPanel = createAreaPanel(REQUEST_LABEL, requestArea);
        JPanel responsePanel = createAreaPanel(RESPONSE_LABEL, responseArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, requestPanel, responsePanel);
        splitPane.setResizeWeight(DIVIDER_POSITION);

        methodBox.addActionListener(event -> {
            JsonMethod selected = (JsonMethod) methodBox.getSelectedItem();
            requestArea.setEnabled(selected != null && selected.requestBodyRequired());
        });
        methodBox.setSelectedItem(JsonMethod.GET);
        requestArea.setEnabled(false);

        sendButton.addActionListener(event -> sendRequest(
                api,
                (JsonMethod) methodBox.getSelectedItem(),
                endpointField.getText(),
                requestArea.getText(),
                sendButton,
                responseArea,
                frame
        ));

        JPanel content = new JPanel(new BorderLayout(BORDER_SIZE, BORDER_SIZE));
        content.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        content.add(requestHeader, BorderLayout.NORTH);
        content.add(splitPane, BorderLayout.CENTER);
        frame.setContentPane(content);
        frame.pack();
        return frame;
    }

    private static JTextArea createTextArea(String text, boolean editable) {
        JTextArea area = new JTextArea(text);
        area.setEditable(editable);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private static JPanel createAreaPanel(String title, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private static void sendRequest(
            JsonApi api,
            JsonMethod method,
            String path,
            String requestJson,
            JButton sendButton,
            JTextArea responseArea,
            JFrame parent
    ) {
        if (method == null) {
            return;
        }
        if (path == null || path.isBlank()) {
            JOptionPane.showMessageDialog(parent, EMPTY_ENDPOINT_ERROR, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        sendButton.setEnabled(false);
        responseArea.setText(SENDING_MESSAGE);
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                return api.request(path, method, requestJson);
            }

            @Override
            protected void done() {
                sendButton.setEnabled(true);
                try {
                    responseArea.setText(JsonMapper.prettyPrint(get()));
                } catch (Exception e) {
                    responseArea.setText("");
                    JOptionPane.showMessageDialog(parent, rootMessage(e), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private static String rootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() == null ? current.getClass().getSimpleName() : current.getMessage();
    }
}
