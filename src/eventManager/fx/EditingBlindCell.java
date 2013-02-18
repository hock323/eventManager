package eventManager.fx;

import eventManager.model.pokerTournament.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
 *
 * @author Octavio
 */
public class EditingBlindCell extends TableCell<Level, Integer> {

    private TextField textField;
    ContextMenu blindTableMenu = new ContextMenu();
    MenuItem deleteItem;
    MenuItem insertAflerItem;
    MenuItem insertBeforeItem;

    public EditingBlindCell() {
        insertAflerItem = new MenuItem("Insert After");
        insertBeforeItem = new MenuItem("Insert Before");
        deleteItem = new MenuItem("Delete");
        blindTableMenu.getItems().add(insertBeforeItem);
        blindTableMenu.getItems().add(insertBeforeItem);
        blindTableMenu.getItems().add(deleteItem);
        insertAflerItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            }
        });
        insertBeforeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            }
        });
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            }
        });
    }

    @Override
    public void startEdit() {
        /*super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();*/
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Integer item, boolean empty) {
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
                this.setContextMenu(blindTableMenu);
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
                        commitEdit(Integer.parseInt(textField.getText()));

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
