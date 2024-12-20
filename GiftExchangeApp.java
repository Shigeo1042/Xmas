import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiftExchangeApp {
    private JFrame frame;
    private JTextField nameField;
    private JTextField numberField;
    private JTextArea resultArea;
    private List<Participant> participants;

    public GiftExchangeApp() {
        participants = new ArrayList<>();
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("プレゼント交換プログラム");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JLabel nameLabel = new JLabel("名前:");
        nameField = new JTextField();
        JLabel numberLabel = new JLabel("番号:");
        numberField = new JTextField();

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(numberLabel);
        inputPanel.add(numberField);
        frame.add(inputPanel, BorderLayout.NORTH);

        JButton addButton = new JButton("追加");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addParticipant();
            }
        });

        JButton startButton = new JButton("プレゼント交換開始");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startExchange();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(startButton);
        frame.add(buttonPanel, BorderLayout.CENTER);

        resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);
        frame.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addParticipant() {
        String name = nameField.getText().trim();
        String numberText = numberField.getText().trim();

        if (name.isEmpty() || !numberText.matches("\\d+")) {
            JOptionPane.showMessageDialog(frame, "名前と正しい番号を入力してください", "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int number = Integer.parseInt(numberText);
        for (Participant p : participants) {
            if (p.getNumber() == number) {
                JOptionPane.showMessageDialog(frame, "番号 " + number + " は既に使われています！", "エラー", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        participants.add(new Participant(name, number));
        nameField.setText("");
        numberField.setText("");
        JOptionPane.showMessageDialog(frame, name + " さんが追加されました！", "追加完了", JOptionPane.INFORMATION_MESSAGE);
    }

    private void startExchange() {
        if (participants.size() < 2) {
            JOptionPane.showMessageDialog(frame, "参加者が2人以上必要です！", "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Participant> shuffled = new ArrayList<>(participants);
        Collections.shuffle(shuffled);

        List<Integer> shiftedNumbers = new ArrayList<>();
        for (int i = 1; i < shuffled.size(); i++) {
            shiftedNumbers.add(shuffled.get(i).getNumber());
        }
        shiftedNumbers.add(shuffled.get(0).getNumber());

        resultArea.setText("");
        StringBuilder result = new StringBuilder();
        for (Participant originalParticipant : participants) {
            for (int i = 0; i < shuffled.size(); i++) {
                if (shuffled.get(i).getNumber() == originalParticipant.getNumber()) {
                    result.append(originalParticipant.getName())
                          .append(" さんはプレゼント ")
                          .append(shiftedNumbers.get(i))
                          .append(" を受け取ります\n");
                    break;
                }
            }
        }

        resultArea.setText(result.toString());
        JOptionPane.showMessageDialog(frame, "プレゼント交換が完了しました！", "完了", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GiftExchangeApp::new);
    }

    static class Participant {
        private final String name;
        private final int number;

        public Participant(String name, int number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }
    }
}
