package eventManager.fx;

import eventManager.model.pokerTournament.Announcement;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
 *
 * @author Octavio
 */
public class EditingStringAnnouncementCell extends TableCell<Announcement, String> {

    private TextField textField;

    public EditingStringAnnouncementCell() {
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText((String) getItem().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {

        textField = new TextField("");
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                try {
                    if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.SPACE) {
                        commitEdit(textField.getText());

                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Valor incorrecto");
                }

            }
        });
    }

    private String getString() {
        if (getItem() == null)
            return "";
        else
            return this.getItem().toString();
    }
}
